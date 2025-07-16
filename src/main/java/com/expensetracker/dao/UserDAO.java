package com.expensetracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.expensetracker.exceptions.DatabaseConnectionException;
import com.expensetracker.exceptions.QueryExecutionException;
import com.expensetracker.exceptions.UserAlreadyExistsException;
import com.expensetracker.exceptions.UserNotFoundException;
import com.expensetracker.model.User;
import com.expensetracker.util.Constants;
import com.expensetracker.util.DBConnection;
import com.expensetracker.util.PasswordUtils;

public class UserDAO {

    private static final Logger LOGGER = LogManager.getLogger(UserDAO.class);

    public int registerUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;

        if (isUserExist(user.getEmail(), user.getPhone())) {
            LOGGER.warn(Constants.LogMessage.Warn.M7, user.getEmail(), user.getPhone());
            throw new UserAlreadyExistsException(Constants.ExceptionMessage.UserAlreadyExistsException.M2);
        }

        int userId = -1;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(Constants.UserQuery.INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, PasswordUtils.encryptPassword(user.getPassword()));
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());

            int rowsAffected = stmt.executeUpdate();
            conn.commit();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1);
                    }
                }
                LOGGER.info(Constants.LogMessage.Info.M5, user.getUsername());
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException sqle) {
                DBConnection.handleSqlException(Constants.ClassMethod.M2, sqle, LOGGER);
            }
            LOGGER.error(Constants.LogMessage.Error.M23, e);
            throw new QueryExecutionException(Constants.ExceptionMessage.QueryExecutionException.M2, e);
        } catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M24, e);
            throw new DatabaseConnectionException(Constants.ExceptionMessage.DatabaseConnectionException.M2, e);
        } finally {
            closeResources(conn, stmt, null);
        }
        return userId;
    }

    public Boolean loginUser(String email, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(Constants.UserQuery.LOGIN_USER);

            stmt.setString(1, email);
            
//            stmt.setString(2, PasswordUtils.encryptPassword(password));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                	String ActualPassword = rs.getString(Constants.UserTable.PASSWORD);
                	if(ActualPassword.equals(PasswordUtils.encryptPassword(password))) {
                		LOGGER.info(Constants.LogMessage.Info.M7, email);
                        return true;
                	}
                }
            }
        } catch (SQLException e) {
            LOGGER.error(Constants.LogMessage.Error.M25, e);
            throw new QueryExecutionException(Constants.ExceptionMessage.QueryExecutionException.M3, e);
        } catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M26, e);
            throw new DatabaseConnectionException(Constants.ExceptionMessage.DatabaseConnectionException.M3, e);
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }

    public boolean isUserExist(String email, String phone) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(Constants.UserQuery.IS_USER_EXISTS);

            stmt.setString(1, email);
            stmt.setString(2, phone);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LOGGER.info(Constants.LogMessage.Info.M8, email, phone);
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.error(Constants.LogMessage.Error.M27, e);
            throw new QueryExecutionException(Constants.ExceptionMessage.QueryExecutionException.M4, e);
        } catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M28, e);
            throw new DatabaseConnectionException(Constants.ExceptionMessage.DatabaseConnectionException.M4, e);
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }

    public User getUserByID(int userID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        User user = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(Constants.UserQuery.GET_USER_BY_ID);

            stmt.setInt(1, userID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserID(rs.getInt(Constants.UserTable.USERID));
                    user.setUsername(rs.getString(Constants.UserTable.USERNAME));
                    user.setPassword(rs.getString(Constants.UserTable.PASSWORD));
                    user.setEmail(rs.getString(Constants.UserTable.EMAIL));
                    user.setPhone(rs.getString(Constants.UserTable.PHONE));
                } else {
                    LOGGER.warn(Constants.LogMessage.Warn.M8, userID);
                    throw new UserNotFoundException("User not found with ID: " + userID);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(Constants.LogMessage.Error.M29, e);
            throw new QueryExecutionException(Constants.ExceptionMessage.QueryExecutionException.M5, e);
        } catch (UserNotFoundException e) {
            LOGGER.warn(Constants.LogMessage.Warn.M9, e);
            throw e;
        } catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M30, e);
            throw new DatabaseConnectionException(Constants.ExceptionMessage.DatabaseConnectionException.M5, e);
        } finally {
            closeResources(conn, stmt, null);
        }

        return user;
    }

    public int getUserIDByEmail(String email) {
        int userID = -1;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(Constants.UserQuery.GET_USERID_BY_EMAIL);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userID = rs.getInt(Constants.UserTable.USERID);
            } else {
                LOGGER.warn(Constants.LogMessage.Warn.M10, email);
                throw new UserNotFoundException( Constants.ExceptionMessage.UserNotFoundException.M1+ email);
            }
        } catch (SQLException e) {
            LOGGER.error(Constants.LogMessage.Error.M34, e);
            throw new QueryExecutionException(Constants.ExceptionMessage.QueryExecutionException.M7, e);
        } catch (UserNotFoundException e) {
            LOGGER.warn(Constants.LogMessage.Warn.M9, e);
            throw e;
        } catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M35, e);
            throw new DatabaseConnectionException(Constants.ExceptionMessage.DatabaseConnectionException.M7, e);
        } finally {
            closeResources(conn, stmt, null);
        }

        return userID;
    }

    public boolean updateUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(Constants.UserQuery.UPDATE_USER);

            stmt.setString(1, user.getUsername());
            stmt.setString(2, PasswordUtils.encryptPassword(user.getPassword()));
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setInt(5, user.getUserID());

            int rowsAffected = stmt.executeUpdate();
            conn.commit();
            if (rowsAffected > 0) {
                LOGGER.info(Constants.LogMessage.Info.M9, user.getUsername());
                return true;
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException sqle) {
                LOGGER.error(Constants.LogMessage.Error.M31, sqle);
            }
            LOGGER.error(Constants.LogMessage.Error.M32, e);
            throw new QueryExecutionException(Constants.ExceptionMessage.QueryExecutionException.M6, e);
        } catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M33, e);
            throw new DatabaseConnectionException(Constants.ExceptionMessage.DatabaseConnectionException.M6, e);
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }

    public boolean deleteUser(int userID) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(Constants.UserQuery.DELETE_USER);
            stmt.setInt(1, userID);

            int rowsAffected = stmt.executeUpdate();
            conn.commit();
            if (rowsAffected > 0) {
                LOGGER.info(Constants.LogMessage.Info.M6, userID);
                return true;
            }
        } catch (SQLException e) {
            LOGGER.error(Constants.LogMessage.Error.M22, e);
            throw new QueryExecutionException(Constants.ExceptionMessage.QueryExecutionException.M1, e);
        } catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M21, e);
            throw new DatabaseConnectionException(Constants.ExceptionMessage.DatabaseConnectionException.M1, e);
        } finally {
            closeResources(conn, stmt, null);
        }
        return false;
    }

    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            LOGGER.error(Constants.LogMessage.Error.M20, e);
        }
    }
}
