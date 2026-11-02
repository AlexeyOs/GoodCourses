package net.os.goodcourses.configuration;

import static org.junit.Assert.assertEquals;

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
}
