package com.sdet.projects.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatabaseValidator {

    static final String EXPECTED_USERNAME = "aisha";
    static final int EXPECTED_COUNT = 3;

    private final String jdbcUrl;

    public DatabaseValidator() {
        this("jdbc:h2:mem:");
    }

    DatabaseValidator(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public ValidationReport validate() throws SQLException, IOException {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, "sa", "")) {
            createSchema(connection);
            insertUsers(connection);
            List<UserRow> users = listUsers(connection);
            boolean found = false;
            for (UserRow user : users) {
                if (EXPECTED_USERNAME.equals(user.username())) {
                    found = true;
                    break;
                }
            }
            return new ValidationReport(users.size(), EXPECTED_COUNT, found, EXPECTED_USERNAME);
        }
    }

    private void createSchema(Connection connection) throws SQLException, IOException {
        String sql = readResource("/schema.sql");
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private void insertUsers(Connection connection) throws SQLException {
        String sql = "INSERT INTO users (username, email) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            insert(statement, "aisha", "aisha@example.com");
            insert(statement, "ben", "ben@example.com");
            insert(statement, "chen", "chen@example.com");
        }
    }

    private void insert(PreparedStatement statement, String username, String email) throws SQLException {
        statement.setString(1, username);
        statement.setString(2, email);
        statement.executeUpdate();
    }

    private List<UserRow> listUsers(Connection connection) throws SQLException {
        String sql = "SELECT id, username, email FROM users ORDER BY id";
        List<UserRow> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(new UserRow(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email")
                ));
            }
        }
        return users;
    }

    private String readResource(String name) throws IOException {
        try (InputStream input = Objects.requireNonNull(
                getClass().getResourceAsStream(name),
                "Missing classpath resource: " + name
        )) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
