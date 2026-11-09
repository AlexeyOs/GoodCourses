package net.os.goodcourses.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.junit.Test;

public class JPAConfigTest {

    @Test
    public void parsesNeonUrlAndPreservesTlsParameters() {
        JPAConfig.DatabaseConnection connection = JPAConfig.parseDatabaseUrl(
                "postgresql://user%40name:p%3Ass@db.example.neon.tech:5432/goodcourses" +
                        "?sslmode=verify-full&channel_binding=require");

        assertEquals("user@name", connection.getUsername());
        assertEquals("p:ss", connection.getPassword());
        assertEquals(
                "jdbc:postgresql://db.example.neon.tech:5432/goodcourses" +
                        "?sslmode=verify-full&channel_binding=require",
                connection.getJdbcUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsDatabaseUrlWithoutServerIdentityVerification() {
        JPAConfig.parseDatabaseUrl(
                "postgresql://user:password@db.example.neon.tech/goodcourses?sslmode=require");
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsUnexpectedScheme() {
        JPAConfig.parseDatabaseUrl(
                "http://user:password@db.example.neon.tech/goodcourses?sslmode=verify-full");
    }

    @Test(expected = IllegalStateException.class)
    public void productionRequiresSeparateMigrationDatabaseUrl() {
        JPAConfig.configureFlyway(null, "production", mock(DataSource.class));
    }

    @Test
    public void productionUsesSeparateMigrationDataSource() {
        DataSource runtimeDataSource = mock(DataSource.class);

        Flyway flyway = JPAConfig.configureFlyway(
                "postgresql://migrator:secret@db.example.neon.tech/goodcourses?sslmode=verify-full",
                "production",
                runtimeDataSource);

        assertNotSame(runtimeDataSource, flyway.getConfiguration().getDataSource());
    }

    @Test
    public void nonProductionFallsBackToRuntimeDataSource() {
        DataSource runtimeDataSource = mock(DataSource.class);

        Flyway flyway = JPAConfig.configureFlyway(null, "docker", runtimeDataSource);

        assertSame(runtimeDataSource, flyway.getConfiguration().getDataSource());
    }
}
