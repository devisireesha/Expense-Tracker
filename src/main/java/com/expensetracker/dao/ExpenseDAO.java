package com.expensetracker.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.expensetracker.model.Expense;
import com.expensetracker.util.Constants;
import com.expensetracker.util.DBConnection;

public class ExpenseDAO {
	public static final Logger LOGGER = LogManager.getLogger(ExpenseDAO.class);
	

	public boolean addExpense(Expense expense) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			
			stmt = conn.prepareStatement(Constants.ExpenseQuery.ADD_EXPENSE);
			stmt.setString(1, expense.getExpenseName());
			stmt.setInt(2, expense.getUserID());
			stmt.setDouble(3, expense.getAmount());
			stmt.setString(4, expense.getType());
			stmt.setString(5, expense.getPayee());
			stmt.setInt(6, expense.getCategoryID());
			stmt.setInt(7, expense.getPaymentModeID());
			stmt.setString(8, Constants.Values.ACTIVE);
			stmt.setString(9, expense.getReferenceID());
			stmt.setString(10, expense.getDescription());
			stmt.setDate(11, expense.getDate());
			int rowsAffected = stmt.executeUpdate();
			conn.commit();

			return rowsAffected > 0;
		} catch (SQLException e) {
			rollback(conn, e);
			DBConnection.handleSqlException(Constants.ClassMethod.M16, e, LOGGER);
			LOGGER.error(Constants.LogMessage.Error.M53, expense, e.getMessage());
		}catch(NullPointerException e) {
			LOGGER.error(Constants.LogMessage.Error.M54, expense, e.getMessage());
		}catch(Exception e) {
			LOGGER.error(Constants.LogMessage.Error.M55, expense, e.getMessage());
		}finally {
            closeResources(conn, stmt);
        }
		return false;
	}

	 public boolean isValidPaymentID(int paymentID) {
	        Connection conn = null;
	        PreparedStatement stmt = null;
	        ResultSet rs = null;

	        try {
	            conn = DBConnection.getConnection();
	            stmt = conn.prepareStatement(Constants.ExpenseQuery.IS_VALID_PAYMENTID);
	            stmt.setInt(1, paymentID);
	            rs = stmt.executeQuery();

	            boolean isValid = rs.next();
	            LOGGER.info(Constants.LogMessage.Info.M10 , isValid, paymentID);
	            return isValid;

	        } catch (SQLException e) {
	            LOGGER.error(Constants.LogMessage.Error.M56, paymentID, e.getMessage());
	        } catch (Exception e) {
	            LOGGER.error(Constants.LogMessage.Error.M57, paymentID, e.getMessage());
	        } finally {
	            closeResources(conn, stmt, rs);
	        }
	        return false;
	  }

	public List<Expense> getExpensesByUser(int userID) {
		List<Expense> expenses = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = DBConnection.getConnection();
			stmt = conn.prepareStatement(Constants.ExpenseQuery.GET_EXPENSE_BY_USER);
			stmt.setInt(1, userID);
			stmt.setString(2, Constants.Values.ACTIVE);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Expense expense = new Expense();
				expense.setExpenseID(rs.getInt(Constants.ExpensesTable.EXPENSEID));
				expense.setExpenseName(rs.getString(Constants.ExpensesTable.EXPENSENAME));
				expense.setUserID(rs.getInt(Constants.ExpensesTable.USERID));
				expense.setAmount(rs.getDouble(Constants.ExpensesTable.AMOUNT));
				expense.setType(rs.getString(Constants.ExpensesTable.TYPE));
				expense.setPayee(rs.getString(Constants.ExpensesTable.PAYEE));
				expense.setCategoryID(rs.getInt(Constants.ExpensesTable.CATEGORYID));
				expense.setCategoryName(rs.getString(Constants.CategoriesTable.CATEGORYNAME)); // Dynamically fetched
				expense.setPaymentModeID(rs.getInt(Constants.ExpensesTable.PAYMENTID));
				expense.setPaymentModeName(rs.getString(Constants.ModeOfPaymentTable.PAYMENTNAME)); // Dynamically fetched
				expense.setStatus(rs.getString(Constants.ExpensesTable.STATUS));
				expense.setReferenceID(rs.getString(Constants.ExpensesTable.REFERENCEID));
				expense.setDescription(rs.getString(Constants.ExpensesTable.DESCRIPTION));
				expense.setDate(rs.getDate(Constants.ExpensesTable.DATE));
				expense.setCreatedAt(rs.getTimestamp(Constants.ExpensesTable.CREATEDAT));
				expense.setUpdatedAt(rs.getTimestamp(Constants.ExpensesTable.UPDATEDAT));

				expenses.add(expense);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DBConnection.handleSqlException(Constants.ClassMethod.M17, e, LOGGER);
		}catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M58, userID, e.getMessage());
        }finally {
            closeResources(conn, stmt);
        }
		return expenses;
	}

	public boolean updateExpense(Expense expense) {
		 Connection conn = null;
	     PreparedStatement stmt = null;

	        try {
	        conn = DBConnection.getConnection();
	        conn.setAutoCommit(false);

	        stmt = conn.prepareStatement(Constants.ExpenseQuery.UPDATE_EXPENSE);			
	        stmt.setString(1, expense.getExpenseName());
			stmt.setDouble(2, expense.getAmount());
			stmt.setString(3, expense.getType());
			stmt.setString(4, expense.getPayee());
			stmt.setInt(5, expense.getCategoryID());
			stmt.setInt(6, expense.getPaymentModeID());
			stmt.setString(7, expense.getStatus());
			stmt.setString(8, expense.getReferenceID());
			stmt.setString(9, expense.getDescription());
			stmt.setDate(10, expense.getDate());
			stmt.setInt(11, expense.getExpenseID());

		    LOGGER.info(Constants.LogMessage.Info.M11, expense.getExpenseID());
			int rowsAffected = stmt.executeUpdate();
			conn.commit();
			
			return rowsAffected > 0;
		} catch (SQLException e) {
			LOGGER.error(Constants.LogMessage.Error.M59, expense, e.getMessage());
			try {
				conn.rollback();
			} catch (SQLException sqle) {
				DBConnection.handleSqlException(Constants.ClassMethod.M18, sqle, LOGGER);
			}
			DBConnection.handleSqlException(Constants.ClassMethod.M19, e, LOGGER);
		}catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M60, expense, e.getMessage());
        }finally {
            closeResources(conn, stmt);
        }
		return false;
	}

	public Expense getExpenseByID(int expenseID) {
		Expense expense = new Expense();
		Connection conn = DBConnection.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection();
			stmt = conn.prepareStatement(Constants.ExpenseQuery.GET_EXPENSE_BY_ID);
			stmt.setInt(1, expenseID);
			rs = stmt.executeQuery();

			if (rs.next()) {
				expense.setExpenseID(rs.getInt(Constants.ExpensesTable.EXPENSEID));
				expense.setExpenseName(rs.getString(Constants.ExpensesTable.EXPENSENAME));
				expense.setUserID(rs.getInt(Constants.ExpensesTable.USERID));
				expense.setAmount(rs.getDouble(Constants.ExpensesTable.AMOUNT));
				expense.setType(rs.getString(Constants.ExpensesTable.TYPE));
				expense.setPayee(rs.getString(Constants.ExpensesTable.PAYEE));
				expense.setCategoryID(rs.getInt(Constants.ExpensesTable.CATEGORYID));
				expense.setCategoryName(rs.getString(Constants.CategoriesTable.CATEGORYNAME)); // Dynamically fetched
				expense.setPaymentModeID(rs.getInt(Constants.ExpensesTable.PAYMENTID));
				expense.setPaymentModeName(rs.getString(Constants.ModeOfPaymentTable.PAYMENTNAME)); // Dynamically fetched
				expense.setStatus(rs.getString(Constants.ExpensesTable.STATUS));
				expense.setReferenceID(rs.getString(Constants.ExpensesTable.REFERENCEID));
				expense.setDescription(rs.getString(Constants.ExpensesTable.DESCRIPTION));
				expense.setDate(rs.getDate(Constants.ExpensesTable.DATE));
				expense.setCreatedAt(rs.getTimestamp(Constants.ExpensesTable.CREATEDAT));
				expense.setUpdatedAt(rs.getTimestamp(Constants.ExpensesTable.UPDATEDAT));
			}
		} catch (SQLException e) {
			LOGGER.error(Constants.LogMessage.Error.M61, expenseID, e.getMessage());
			e.printStackTrace();
			DBConnection.handleSqlException(Constants.ClassMethod.M20, e, LOGGER);
		}catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M62, expense, e.getMessage());
        }finally {
            closeResources(conn, stmt,rs);
        }

		return expense;

	}

	public boolean deleteExpense(int expenseID) {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try{
			conn = DBConnection.getConnection(); 
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(Constants.ExpenseQuery.DELETE_EXPENSE);
			stmt.setString(1, Constants.Values.INACTIVE);
			stmt.setInt(2, expenseID);
			int rowsAffected = stmt.executeUpdate();
			conn.commit();
			return rowsAffected > 0;
		} catch (SQLException e) {
			LOGGER.error(Constants.LogMessage.Error.M63, expenseID, e.getMessage());
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException sqle) {
				DBConnection.handleSqlException(Constants.ClassMethod.M21, sqle, LOGGER);
			}
			DBConnection.handleSqlException(Constants.ClassMethod.M22, e, LOGGER);
		} catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M64, expenseID, e.getMessage());
        } finally {
            closeResources(conn, stmt);
        }
		return false;
	}

	public List<Expense> getAllExpenses(int userID) {
		List<Expense> expenses = new ArrayList<>();
		Connection conn = DBConnection.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(Constants.ExpenseQuery.GET_ALL_EXPENSES);
			stmt.setString(1, Constants.Values.ACTIVE);
			stmt.setInt(2, userID);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Expense expense = new Expense();
				expense.setExpenseID(rs.getInt(Constants.ExpensesTable.EXPENSEID));
				expense.setExpenseName(rs.getString(Constants.ExpensesTable.EXPENSENAME));
				expense.setUserID(rs.getInt(Constants.ExpensesTable.USERID));
				expense.setAmount(rs.getDouble(Constants.ExpensesTable.AMOUNT));
				expense.setType(rs.getString(Constants.ExpensesTable.TYPE));
				expense.setPayee(rs.getString(Constants.ExpensesTable.PAYEE));
				expense.setCategoryID(rs.getInt(Constants.ExpensesTable.CATEGORYID));
				expense.setCategoryName(rs.getString(Constants.CategoriesTable.CATEGORYNAME)); // Dynamically fetched
				expense.setPaymentModeID(rs.getInt(Constants.ExpensesTable.PAYMENTID));
				expense.setPaymentModeName(rs.getString(Constants.ModeOfPaymentTable.PAYMENTNAME)); // Dynamically fetched
				expense.setStatus(rs.getString(Constants.ExpensesTable.STATUS));
				expense.setReferenceID(rs.getString(Constants.ExpensesTable.REFERENCEID));
				expense.setDescription(rs.getString(Constants.ExpensesTable.DESCRIPTION));
				expense.setDate(rs.getDate(Constants.ExpensesTable.DATE));
				expense.setCreatedAt(rs.getTimestamp(Constants.ExpensesTable.CREATEDAT));
				expense.setUpdatedAt(rs.getTimestamp(Constants.ExpensesTable.UPDATEDAT));

				expenses.add(expense);
			}
		} catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M23, e, LOGGER);
		} catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M65, e.getMessage());
        } finally {
            closeResources(conn, stmt);
        }
		return expenses;
	}

	public List<Expense> getExpensesByCategory(int userID, int categoryID) {
		List<Expense> expenses = new ArrayList<>();
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection(); 
			stmt = conn.prepareStatement(Constants.ExpenseQuery.GET_EXPENSES_BY_CATEGORY);
			stmt.setInt(1, userID);
			stmt.setInt(2, categoryID);
			stmt.setString(3, Constants.Values.ACTIVE);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Expense expense = new Expense();
				expense.setExpenseID(rs.getInt(Constants.ExpensesTable.EXPENSEID));
				expense.setExpenseName(rs.getString(Constants.ExpensesTable.EXPENSENAME));
				expense.setUserID(rs.getInt(Constants.ExpensesTable.USERID));
				expense.setAmount(rs.getDouble(Constants.ExpensesTable.AMOUNT));
				expense.setType(rs.getString(Constants.ExpensesTable.TYPE));
				expense.setPayee(rs.getString(Constants.ExpensesTable.PAYEE));
				expense.setCategoryID(rs.getInt(Constants.ExpensesTable.CATEGORYID));
				expense.setCategoryName(rs.getString(Constants.CategoriesTable.CATEGORYNAME)); // Dynamically fetched
				expense.setPaymentModeID(rs.getInt(Constants.ExpensesTable.PAYMENTID));
				expense.setPaymentModeName(rs.getString(Constants.ModeOfPaymentTable.PAYMENTNAME)); // Dynamically fetched
				expense.setStatus(rs.getString(Constants.ExpensesTable.STATUS));
				expense.setReferenceID(rs.getString(Constants.ExpensesTable.REFERENCEID));
				expense.setDescription(rs.getString(Constants.ExpensesTable.DESCRIPTION));
				expense.setDate(rs.getDate(Constants.ExpensesTable.DATE));
				expense.setCreatedAt(rs.getTimestamp(Constants.ExpensesTable.CREATEDAT));
				expense.setUpdatedAt(rs.getTimestamp(Constants.ExpensesTable.UPDATEDAT));

				expenses.add(expense);
			}
		}catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M24, e, LOGGER);
		}catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M66, e.getMessage());
        } finally {
            closeResources(conn, stmt,rs);
        }
		return expenses;
	}

	public List<Expense> getExpensesByPaymentMode(int userID, int paymentID) {
		List<Expense> expenses = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection(); 
			stmt = conn.prepareStatement(Constants.ExpenseQuery.GET_EXPENSES_BY_PAYMENTMODE);

			stmt.setInt(1, userID);
			stmt.setInt(2, paymentID);
			stmt.setString(3, Constants.Values.ACTIVE);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Expense expense = new Expense();
				expense.setExpenseID(rs.getInt(Constants.ExpensesTable.EXPENSEID));
				expense.setExpenseName(rs.getString(Constants.ExpensesTable.EXPENSENAME));
				expense.setUserID(rs.getInt(Constants.ExpensesTable.USERID));
				expense.setAmount(rs.getDouble(Constants.ExpensesTable.AMOUNT));
				expense.setType(rs.getString(Constants.ExpensesTable.TYPE));
				expense.setPayee(rs.getString(Constants.ExpensesTable.PAYEE));
				expense.setCategoryID(rs.getInt(Constants.ExpensesTable.CATEGORYID));
				expense.setCategoryName(rs.getString(Constants.CategoriesTable.CATEGORYNAME)); 
				expense.setPaymentModeID(rs.getInt(Constants.ExpensesTable.PAYMENTID));
				expense.setPaymentModeName(rs.getString(Constants.ModeOfPaymentTable.PAYMENTNAME)); 
				expense.setStatus(rs.getString(Constants.ExpensesTable.STATUS));
				expense.setReferenceID(rs.getString(Constants.ExpensesTable.REFERENCEID));
				expense.setDescription(rs.getString(Constants.ExpensesTable.DESCRIPTION));
				expense.setDate(rs.getDate(Constants.ExpensesTable.DATE));
				expense.setCreatedAt(rs.getTimestamp(Constants.ExpensesTable.CREATEDAT));
				expense.setUpdatedAt(rs.getTimestamp(Constants.ExpensesTable.UPDATEDAT));

				expenses.add(expense);
			}
		}catch (SQLException e) {
				DBConnection.handleSqlException(Constants.ClassMethod.M25, e, LOGGER);
			}catch (Exception e) {
	            LOGGER.error(Constants.LogMessage.Error.M67, e.getMessage());
	        } finally {
	            closeResources(conn, stmt,rs);
	        }
		return expenses;
	}

	public List<Expense> getExpensesByCategoryAndPaymentMode(int userID, int categoryID, int paymentID) {
		List<Expense> expenses = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection(); 
			stmt = conn.prepareStatement(Constants.ExpenseQuery.GET_EXPENSES_BY_CATEGORY_AND_PAYMENTMODE);

			stmt.setInt(1, userID);
			stmt.setInt(2, paymentID);
			stmt.setString(3, Constants.Values.ACTIVE);
			stmt.setInt(4, categoryID);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Expense expense = new Expense();
				expense.setExpenseID(rs.getInt(Constants.ExpensesTable.EXPENSEID));
				expense.setExpenseName(rs.getString(Constants.ExpensesTable.EXPENSENAME));
				expense.setUserID(rs.getInt(Constants.ExpensesTable.USERID));
				expense.setAmount(rs.getDouble(Constants.ExpensesTable.AMOUNT));
				expense.setType(rs.getString(Constants.ExpensesTable.TYPE));
				expense.setPayee(rs.getString(Constants.ExpensesTable.PAYEE));
				expense.setCategoryID(rs.getInt(Constants.ExpensesTable.CATEGORYID));
				expense.setCategoryName(rs.getString(Constants.CategoriesTable.CATEGORYNAME));
				expense.setPaymentModeID(rs.getInt(Constants.ExpensesTable.PAYMENTID));
				expense.setPaymentModeName(rs.getString(Constants.ModeOfPaymentTable.PAYMENTNAME)); 
				expense.setStatus(rs.getString(Constants.ExpensesTable.STATUS));
				expense.setReferenceID(rs.getString(Constants.ExpensesTable.REFERENCEID));
				expense.setDescription(rs.getString(Constants.ExpensesTable.DESCRIPTION));
				expense.setDate(rs.getDate(Constants.ExpensesTable.DATE));
				expense.setCreatedAt(rs.getTimestamp(Constants.ExpensesTable.CREATEDAT));
				expense.setUpdatedAt(rs.getTimestamp(Constants.ExpensesTable.UPDATEDAT));

				expenses.add(expense);
			}
		} catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M26, e, LOGGER);
		}catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M68, e.getMessage());
        } finally {
            closeResources(conn, stmt,rs);
        }
		return expenses;
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

	public int getUserIdByExpenseId(int expenseID) {
		int userID = -1;
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DBConnection.getConnection();
			stmt = conn.prepareStatement(Constants.ExpenseQuery.GET_USERID_BY_EXPENSEID);
			stmt.setInt(1, expenseID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				userID = rs.getInt(Constants.UserTable.USERID);
			}
		} catch (SQLException e) {
			LOGGER.error(Constants.LogMessage.Error.M69, e);
		} finally {
			closeResources(conn, stmt, null);
		}

		return userID;
	}

	public Map<String, Object> getCategoryWiseReport(int userID, int categoryID) {
		Map<String, Object> result = new HashMap<>();
		List<Expense> expenseList = new ArrayList<>();
		double total = 0;
		double highest = 0;
		double lowest = Double.MAX_VALUE;
		PreparedStatement preparedStatement = null;
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			preparedStatement = conn.prepareStatement(Constants.ExpenseQuery.GET_CATEGORYWISE_REPORT);
			preparedStatement.setInt(1, userID);
			preparedStatement.setString(2, Constants.Values.ACTIVE);
			preparedStatement.setString(3, Constants.Values.SPENT);
			preparedStatement.setInt(4, categoryID);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					Expense expense = new Expense();
					int categoryId = resultSet.getInt(Constants.ExpensesTable.CATEGORYID);
					String categoryName = resultSet.getString(Constants.CategoriesTable.CATEGORYNAME);
					double amount = resultSet.getDouble(Constants.ExpensesTable.AMOUNT);

					expense.setCategoryID(categoryId);
					expense.setCategoryName(categoryName);
					expense.setAmount(amount);

					expenseList.add(expense);
					total += amount;
					highest = Math.max(highest, amount);
					lowest = Math.min(lowest, amount);
				}
			}
		} catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M27, e, LOGGER);
		}catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M70 , e.getMessage());
        } finally {
            closeResources(conn, preparedStatement);
        }
		result.put(Constants.Values.EXPENSELIST, expenseList);
		result.put(Constants.Values.TOTAL, total);
		result.put(Constants.Values.HIGHEST, highest);
		result.put(Constants.Values.LOWEST, lowest);
		result.put(Constants.Values.AVERAGE, expenseList.isEmpty() ? 0 : total / expenseList.size());

		return result;
	}

	public Map<String, Object> getDateRangeReport(int userID, String startDate, String endDate) throws SQLException {
		List<Expense> expenseList = new ArrayList<>();
		double totalAmount = 0;
		double averageAmount = 0;
		double highestAmount = 0;
		double lowestAmount = Double.MAX_VALUE;
		boolean foundExpenses = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, Object> result = new HashMap<>();
		try {
			conn = DBConnection.getConnection(); 
			stmt = conn.prepareStatement(Constants.ExpenseQuery.GET_DATE_RANGE_REPORT);
			stmt.setInt(1, userID);
			stmt.setString(2, Constants.Values.SPENT);
			stmt.setString(3, Constants.Values.ACTIVE);
			stmt.setString(4, startDate);
			stmt.setString(5, endDate);

			rs = stmt.executeQuery();
			while (rs.next()) {
				foundExpenses = true;
				Expense expense = new Expense();
				expense.setExpenseName(rs.getString(Constants.ExpensesTable.EXPENSENAME));
				expense.setExpenseID(rs.getInt(Constants.ExpensesTable.EXPENSEID));
				expense.setCategoryName(rs.getString(Constants.CategoriesTable.CATEGORYNAME));
				expense.setAmount(rs.getDouble(Constants.ExpensesTable.AMOUNT));
				expense.setDate(rs.getDate(Constants.ExpensesTable.DATE));
				expenseList.add(expense);

				totalAmount += rs.getDouble(Constants.ExpensesTable.AMOUNT);
				if (rs.getDouble(Constants.ExpensesTable.AMOUNT) > highestAmount) {
					highestAmount = rs.getDouble(Constants.ExpensesTable.AMOUNT);
				}
				if (rs.getDouble(Constants.ExpensesTable.AMOUNT) < lowestAmount) {
					lowestAmount = rs.getDouble(Constants.ExpensesTable.AMOUNT);
				}
			}
			if(!foundExpenses) {
				lowestAmount = 0;
			}
			
			if (!expenseList.isEmpty()) {
				averageAmount = totalAmount / expenseList.size();
			}
			
			result.put(Constants.Values.EXPENSELIST, expenseList);
			result.put(Constants.Values.TOTAL, totalAmount);
			result.put(Constants.Values.AVERAGE, averageAmount);
			result.put(Constants.Values.HIGHEST, highestAmount);
			result.put(Constants.Values.LOWEST, lowestAmount);
			
		}catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M28, e, LOGGER);
		}catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M52, e.getMessage());
        } finally {
            closeResources(conn, stmt);
        }
			return result;
	}

	public double[] calculateIncomeSpentRemaining(int userID) {
		double income = 0;
		double spent = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection(); 
			stmt = conn.prepareStatement(Constants.ExpenseQuery.CALCULATE_INCOME_SPENT_REMAINING);
			stmt.setInt(1, userID);
			stmt.setString(2, Constants.Values.ACTIVE);
			rs = stmt.executeQuery();
			while (rs.next()) {
				double amount = (double) rs.getDouble(Constants.ExpensesTable.AMOUNT);
				String type = rs.getString(Constants.ExpensesTable.TYPE);

				if (type == null || type.isEmpty()) {
					continue;
				}
				if (type.equalsIgnoreCase(Constants.Values.INCOME)) {
					income += amount;
				} else if (type.equalsIgnoreCase(Constants.Values.SPENT)) {
					spent += amount;
				}
				System.out.println(income);
				System.out.println(spent);
			}

		} catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M29, e, LOGGER);
		}catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M51, e.getMessage());
        } finally {
            closeResources(conn, stmt);
        }

		double remaining = income - spent;
		System.out.println(income);
		System.out.println(spent);
		System.out.println(remaining);

		return new double[] { income, spent, remaining };
	}
	
	 private void rollback(Connection conn, Exception e) {
	        if (conn != null) {
	            try {
	                conn.rollback();
	                LOGGER.warn(Constants.LogMessage.Warn.M11 +e.getMessage()+ Constants.Util.DOT);
	            } catch (SQLException sqle) {
	                LOGGER.error(Constants.LogMessage.Error.M50, sqle.getMessage());
	            }
	        }
	    }
}
