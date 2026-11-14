package net.os.goodcourses.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.env.MockEnvironment;

public class HealthControllerTest {

    @Test
    public void livenessDoesNotRequireDatabase() {
        HealthController controller = new HealthController(mock(DataSource.class), new MockEnvironment());

        assertEquals(HttpStatus.OK, controller.live().getStatusCode());
    }

    @Test
    public void readinessIsUpWhenDatabaseResponds() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        PreparedStatement recoveryStatement = mock(PreparedStatement.class);
        ResultSet recoveryResult = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT 1")).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
        when(connection.prepareStatement(
                "SELECT COUNT(*) FROM profile WHERE password_state = 'RESET_REQUIRED'"))
                .thenReturn(recoveryStatement);
        when(recoveryStatement.executeQuery()).thenReturn(recoveryResult);
        when(recoveryResult.next()).thenReturn(true);
        when(recoveryResult.getLong(1)).thenReturn(0L);

        ResponseEntity<String> response = new HealthController(dataSource, new MockEnvironment()).ready();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("READY", response.getBody());
    }

    @Test
    public void readinessIsUnavailableWithoutLeakingDatabaseError() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getConnection()).thenThrow(new SQLException("secret connection details"));

        ResponseEntity<String> response = new HealthController(dataSource, new MockEnvironment()).ready();

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("NOT_READY", response.getBody());
    }
}
