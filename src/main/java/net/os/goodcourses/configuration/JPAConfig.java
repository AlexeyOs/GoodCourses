package net.os.goodcourses.configuration;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 */
@Configuration
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:${configProfile}.properties")
})
@EnableTransactionManagement
@EnableJpaRepositories("net.os.goodcourses.repository.storage")
public class JPAConfig {

    @Autowired
    private Environment environment;

    /**
     * http://docs.spring.io/autorepo/docs/spring/4.2.5.RELEASE/spring-framework-reference/html/beans.html
     *
     * By default, beans defined using Java config that have a public close or shutdown method are automatically enlisted with a destruction callback.
     */

    @Bean(/*destroyMethod="close"*/)
    public DataSource dataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl != null && !databaseUrl.trim().isEmpty()) {
            DatabaseConnection connection = parseDatabaseUrl(databaseUrl);
            basicDataSource.setUrl(connection.jdbcUrl);
            basicDataSource.setUsername(connection.username);
            basicDataSource.setPassword(connection.password);
        } else {
            basicDataSource.setDriverClassName(environment.getRequiredProperty("db.driver"));
            basicDataSource.setUrl(environment.getRequiredProperty("db.url"));
            basicDataSource.setUsername(environment.getRequiredProperty("db.username"));
            basicDataSource.setPassword(environment.getRequiredProperty("db.password"));
        }
        basicDataSource.setInitialSize(Integer.parseInt(environment.getRequiredProperty("db.pool.initSize")));
        basicDataSource.setMaxTotal(Integer.parseInt(environment.getRequiredProperty("db.pool.maxSize")));
        basicDataSource.setMaxWaitMillis(5000);
        basicDataSource.setValidationQuery("SELECT 1");
        basicDataSource.setValidationQueryTimeout(3);
        basicDataSource.setTestOnBorrow(true);
        return basicDataSource;
    }

    static DatabaseConnection parseDatabaseUrl(String databaseUrl) {
        try {
            URI uri = new URI(databaseUrl);
            if (!("postgres".equals(uri.getScheme()) || "postgresql".equals(uri.getScheme()))) {
                throw new IllegalArgumentException("DATABASE_URL must use postgres or postgresql scheme");
            }
            if (uri.getHost() == null || uri.getRawUserInfo() == null || uri.getPath() == null) {
                throw new IllegalArgumentException("DATABASE_URL is incomplete");
            }
            String[] userInfo = uri.getRawUserInfo().split(":", 2);
            if (userInfo.length != 2) {
                throw new IllegalArgumentException("DATABASE_URL credentials are incomplete");
            }
            String query = uri.getRawQuery();
            if (query == null || !containsQueryParameter(query, "sslmode", "verify-full")) {
                throw new IllegalArgumentException("DATABASE_URL must set sslmode=verify-full");
            }
            int port = uri.getPort() < 0 ? 5432 : uri.getPort();
            String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ':' + port + uri.getPath() + '?' + query;
            return new DatabaseConnection(
                    jdbcUrl,
                    URLDecoder.decode(userInfo[0], StandardCharsets.UTF_8.name()),
                    URLDecoder.decode(userInfo[1], StandardCharsets.UTF_8.name()));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("DATABASE_URL is invalid", e);
        }
    }

    private static boolean containsQueryParameter(String query, String name, String expectedValue) {
        for (String parameter : query.split("&")) {
            String[] pair = parameter.split("=", 2);
            if (pair.length == 2 && name.equalsIgnoreCase(pair[0]) && expectedValue.equalsIgnoreCase(pair[1])) {
                return true;
            }
        }
        return false;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        boolean baselineOnMigrate = Boolean.parseBoolean(
                environment.getProperty("FLYWAY_BASELINE_ON_MIGRATE", "false"));
        if (baselineOnMigrate) {
            validateBaselineSchema(dataSource());
        }
        return Flyway.configure()
                .dataSource(dataSource())
                .locations("classpath:db/migration")
                .baselineOnMigrate(baselineOnMigrate)
                .baselineVersion("1")
                .validateOnMigrate(true)
                .load();
    }

    private void validateBaselineSchema(DataSource source) {
        try (Connection connection = source.getConnection()) {
            if (count(connection,
                    "SELECT COUNT(*) FROM information_schema.tables " +
                            "WHERE table_schema='public' AND table_type='BASE TABLE' " +
                            "AND table_name <> 'flyway_schema_history'") == 0) {
                return;
            }
            requireCount(connection,
                    "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='public' " +
                            "AND table_name IN ('profile','course','feedback','skill','skill_category'," +
                            "'course_profile','profile_restore','persistent_logins')",
                    8, "required tables");
            requireCount(connection,
                    "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema='public' AND " +
                            "(table_name, column_name) IN (('profile','password'),('feedback','rating')," +
                            "('feedback','profile_id'),('feedback','course_id'))",
                    4, "required columns");
            requireCount(connection,
                    "SELECT COUNT(*) FROM pg_constraint c JOIN pg_namespace n " +
                            "ON n.oid = c.connamespace WHERE n.nspname='public' AND conname IN " +
                            "('uq_feedback_course_profile','chk_feedback_rating')",
                    2, "issue #31/#54 constraints");
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to validate database schema before Flyway baseline", exception);
        }
    }

    private void requireCount(Connection connection, String sql, int expected, String subject)
            throws SQLException {
        if (count(connection, sql) != expected) {
            throw new IllegalStateException("Database schema does not match V1 baseline: " + subject);
        }
    }

    private int count(Connection connection, String sql) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setQueryTimeout(5);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? result.getInt(1) : 0;
            }
        }
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("javax.persistence.validation.mode", "none");
        return properties;
    }

    @Bean
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setPackagesToScan("net.os.goodcourses.entity");
        entityManagerFactory.setJpaProperties(hibernateProperties());
        return entityManagerFactory;
    }

    @Bean
    public JpaTransactionManager transactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    static final class DatabaseConnection {
        private final String jdbcUrl;
        private final String username;
        private final String password;

        private DatabaseConnection(String jdbcUrl, String username, String password) {
            this.jdbcUrl = jdbcUrl;
            this.username = username;
            this.password = password;
        }

        String getJdbcUrl() {
            return jdbcUrl;
        }

        String getUsername() {
            return username;
        }

        String getPassword() {
            return password;
        }
    }
}
