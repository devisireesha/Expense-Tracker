package com.expensetracker.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.util.Constants;
import com.expensetracker.util.DBConnection;

public class CategoryDAO {

	public static final Logger LOGGER = LogManager.getLogger(CategoryDAO.class);
	
	
	public List<Category> getAllCategories() {
		List<Category> categories = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(Constants.CategoryQuery.GET_ALL_CATEGORIES)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Category category = new Category();
				category.setCategoryID(rs.getInt(Constants.CategoriesTable.CATEGORYID));
				category.setCategoryName(rs.getString(Constants.CategoriesTable.CATEGORYNAME));
				category.setDefault(rs.getBoolean(Constants.CategoriesTable.ISDEFAULT));
				categories.add(category);
			}
		} catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M3, e, LOGGER);
		} catch(Exception e) {
			LOGGER.error(Constants.LogMessage.Error.M36, e.getMessage());
		}
		return categories;
	}

	public boolean addCategory(Category category, int userId) {

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt1 = conn.prepareStatement(Constants.CategoryQuery.ADD_CATEGORY);) {
			stmt1.setString(1, category.getCategoryName());
			stmt1.setInt(2, userId);
			stmt1.setBoolean(3, false);
			int rowsAffected = stmt1.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M4, e, LOGGER);
		} catch(Exception e) {
			LOGGER.error(Constants.LogMessage.Error.M37, e.getMessage());
		}
		return false;
	}

	public boolean updateCategory(Category category) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(Constants.CategoryQuery.UPDATE_CATEGORY)) {
			stmt.setString(1, category.getCategoryName());
			stmt.setInt(2, category.getCategoryID());

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M5, e, LOGGER);
		}catch(Exception e) {
			LOGGER.error(Constants.LogMessage.Error.M38, e.getMessage());
		}
		return false;
	}

	public boolean deleteCategory(int categoryID) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(Constants.CategoryQuery.DELETE_CATEGORY)) {
			stmt.setBoolean(1, false);
			stmt.setInt(2, categoryID);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M6, e, LOGGER);
		} catch(Exception e) {
			LOGGER.error(Constants.LogMessage.Error.M39, e.getMessage());
		}
		return false;
	}

	public List<Category> getCategoriesForUser(int userID) {
		List<Category> categories = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(Constants.CategoryQuery.GET_CATEGORIES_FOR_USER)) {
			stmt.setInt(1, userID);
			stmt.setBoolean(2, true);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Category category = new Category();
				category.setCategoryID(rs.getInt(Constants.CategoriesTable.CATEGORYID));
				category.setCategoryName(rs.getString(Constants.CategoriesTable.CATEGORYNAME));
				category.setDefault(rs.getBoolean(Constants.CategoriesTable.ISDEFAULT));
				categories.add(category);
				}
		} catch (SQLException e) {
			e.printStackTrace();
			DBConnection.handleSqlException(Constants.ClassMethod.M7, e, LOGGER);

		}catch(Exception e) {
			LOGGER.error(Constants.LogMessage.Error.M40, e.getMessage());
		}
		return categories;
	}
	
	public List<Category> getCategoriesForUserToManage(int userID) {
		List<Category> categories = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(Constants.CategoryQuery.GET_CATEGORIES_FOR_USER_TO_MANAGE)) {
			stmt.setInt(1, userID);
			stmt.setBoolean(2, true);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Category category = new Category();
				category.setCategoryID(rs.getInt(Constants.CategoriesTable.CATEGORYID));
				category.setCategoryName(rs.getString(Constants.CategoriesTable.CATEGORYNAME));
				categories.add(category);
		} 
	} catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M8, e, LOGGER);
		}catch(Exception e) {
			LOGGER.error(Constants.LogMessage.Error.M41, e.getMessage());
		}
		return categories;
	}
	
	public Category getCategoryByID(int categoryID) {
		Category category = new Category();
		Connection conn = DBConnection.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(Constants.CategoryQuery.GET_CATEGORY_BY_ID);
			stmt.setInt(1, categoryID);
			stmt.setBoolean(2, true);
			rs = stmt.executeQuery();
			if (rs.next()) {
				category.setCategoryID(rs.getInt(Constants.CategoriesTable.CATEGORYID));
				category.setCategoryName(rs.getString(Constants.CategoriesTable.CATEGORYNAME));
				category.setDefault(rs.getBoolean(Constants.CategoriesTable.ISDEFAULT));
			}
		} catch (SQLException e) {
			LOGGER.error(Constants.LogMessage.Error.M42, categoryID, e.getMessage());
			DBConnection.handleSqlException(Constants.ClassMethod.M9, e, LOGGER);
		}catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M43, category, e.getMessage());
        }finally {
            closeResources(conn, stmt,rs);
        }
		return category;

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
