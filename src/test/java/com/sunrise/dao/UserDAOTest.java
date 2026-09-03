package com.sunrise.dao;

import com.sunrise.util.DBConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserDAOTest {

    private static final String TEST_USERNAME = "junit_test_user";
    private static final String TEST_PASSWORD = "TestPass123";
    private static final String TEST_ROLE = "Staff";

    @BeforeEach
    void createTestUser() throws SQLException {
        deleteTestUser();
        UserDAO userDAO = new UserDAO();
        userDAO.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_ROLE);
    }

    @AfterEach
    void cleanUpTestUser() throws SQLException {
        deleteTestUser();
    }

    private void deleteTestUser() throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM User WHERE username = ?");
        stmt.setString(1, TEST_USERNAME);
        stmt.executeUpdate();
    }

    @Test
    void validateLogin_returnsRoleForCorrectCredentials() {
        UserDAO userDAO = new UserDAO();

        String role = userDAO.validateLogin(TEST_USERNAME, TEST_PASSWORD);

        assertEquals(TEST_ROLE, role);
    }

    @Test
    void validateLogin_returnsNullForWrongPassword() {
        UserDAO userDAO = new UserDAO();

        String role = userDAO.validateLogin(TEST_USERNAME, "WrongPassword");

        assertNull(role);
    }

    @Test
    void validateLogin_returnsNullForNonExistentUsername() {
        UserDAO userDAO = new UserDAO();

        String role = userDAO.validateLogin("no_such_user_at_all", TEST_PASSWORD);

        assertNull(role);
    }
}