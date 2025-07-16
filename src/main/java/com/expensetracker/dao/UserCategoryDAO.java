package com.expensetracker.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.expensetracker.model.Category;
import com.expensetracker.model.UserCategory;
import com.expensetracker.util.Constants;
import com.expensetracker.util.DBConnection;

public class UserCategoryDAO {
	public static final Logger LOGGER = LogManager.getLogger(UserCategoryDAO.class);
    public boolean addUserCategory(UserCategory userCategory) {
    	Connection conn = null;
    	PreparedStatement stmt = null;
        try {
        	conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(Constants.UserCategoryQuery.ADD_USER_CATEGORY);
            
            stmt.setInt(1, userCategory.getUserID());
            stmt.setInt(2, userCategory.getCategoryID());
            stmt.setBoolean(3, userCategory.isDefault());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M12, e, LOGGER);
		} catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M46, e.getMessage());
        } finally {
            closeResources(conn, stmt);
        }
        return false;
    }

    public List<Category> getCategoriesForUser(int userID) {
        List<Category> categories = new ArrayList<>();
        Connection conn = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
        try {
        	conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(Constants.UserCategoryQuery.GET_CATEGORIES_FOR_USER);
            stmt.setInt(1, userID);
            stmt.setBoolean(2,true);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryID(rs.getInt(Constants.UserCategoriesTable.CATEGORYID));
                category.setCategoryName(rs.getString(Constants.CategoriesTable.CATEGORYNAME));
                categories.add(category);
            }
        } catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M13, e, LOGGER);
		} catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M47, e.getMessage());
        } finally {
            closeResources(conn, stmt,rs);
        }
        return categories;
    }

    // Remove User Category
    public boolean removeUserCategory(int userCategoryID) {
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
        try {
        	conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(Constants.UserCategoryQuery.REMOVE_USER_CATEGORY);
            stmt.setBoolean(1,false);
            stmt.setInt(2, userCategoryID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M14, e, LOGGER);
		} catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M48, e.getMessage());
        } finally {
            closeResources(conn, stmt,rs);
        }
        return false;
    }
    
    public void assignDefaultCategories(int userID) throws SQLException {
    	Connection conn = null;
    	PreparedStatement stmt = null;
        try 
        { 
        	conn = DBConnection.getConnection();
        	stmt = conn.prepareStatement(Constants.UserCategoryQuery.ASSIGN_DEFAULT_CATEGORIES);
            stmt.setInt(1, userID);
            stmt.setBoolean(2,true);
            stmt.executeUpdate();
        }catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M15, e, LOGGER);
		} catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M49, e.getMessage());
        } finally {
            closeResources(conn, stmt);
        }
    }
    
    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			LOGGER.error(Constants.LogMessage.Error.M20, e);
		}
	}
	
	private void closeResources(Connection conn, PreparedStatement stmt) {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			LOGGER.error(Constants.LogMessage.Error.M20, e);
		}
	}
}

