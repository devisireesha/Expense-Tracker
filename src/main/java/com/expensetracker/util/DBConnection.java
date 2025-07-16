package com.expensetracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.expensetracker.exceptions.DatabaseConnectionException;

public class DBConnection {
    
    public static final Logger LOGGER = LogManager.getLogger(DBConnection.class);

    public static Connection getConnection() {
        try {
            Class.forName(Constants.Database.DRIVER);
            return DriverManager.getConnection(Constants.Database.URL, Constants.Database.USERNAME, Constants.Database.PASSWORD);
        } catch (SQLException e) {
            handleSqlException(Constants.ClassMethod.M1, e, LOGGER);
        } catch (ClassNotFoundException e) {
            String errorMessage = Constants.ErrorMessage.E30;
            LOGGER.error(errorMessage, e);
            throw new DatabaseConnectionException(errorMessage, e);
        } catch (Exception e) {
            String errorMessage = Constants.ErrorMessage.E29;
            LOGGER.error(errorMessage, e);
            throw new DatabaseConnectionException(errorMessage, e);
        }
        return null;
    }

    public static void handleSqlException(String method, SQLException e, Logger log) {
        String errorMessage = String.format(Constants.Database.EXCEPTION_FORMAT, method, e.getMessage(), e.getErrorCode());
        log.warn(errorMessage);
        throw new DatabaseConnectionException(errorMessage, e);
    }
}


