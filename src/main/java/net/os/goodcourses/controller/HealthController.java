package net.os.goodcourses.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HealthController {

    private final DataSource dataSource;
    private final Environment environment;

    @Autowired
    public HealthController(DataSource dataSource, Environment environment) {
        this.dataSource = dataSource;
        this.environment = environment;
    }

    @GetMapping("/health/live")
    @ResponseBody
    public ResponseEntity<String> live() {
        return ResponseEntity.ok("UP");
    }

    @GetMapping("/health/ready")
    @ResponseBody
    public ResponseEntity<String> ready() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT 1")) {
            statement.setQueryTimeout(3);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next() && result.getInt(1) == 1) {
                    if (recoveryChannelIsReady(connection)) {
                        return ResponseEntity.ok("READY");
                    }
                }
            }
        } catch (Exception ignored) {
            // Do not expose database details or credentials from a public endpoint.
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("NOT_READY");
    }

    private boolean recoveryChannelIsReady(Connection connection) throws Exception {
        if (Boolean.parseBoolean(environment.getProperty("email.enabled", "false"))) {
            return true;
        }
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) FROM profile WHERE password_state = 'RESET_REQUIRED'")) {
            statement.setQueryTimeout(3);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() && result.getLong(1) == 0;
            }
        }
    }
}
