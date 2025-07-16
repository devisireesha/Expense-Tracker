package com.expensetracker.util;


public final class Constants {
	
	public static final class Database{
		public static final String URL = "jdbc:h2:~/test;AUTO_RECONNECT=TRUE;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE;";
		public static final String USERNAME = "sa";
		public static final String PASSWORD = "";
		public static final String EXCEPTION_FORMAT = "exception in %s, message: %s, code : %s";
		public static final String DRIVER = "org.h2.Driver";
	}
	
	public static final class UserQuery{
		public static final String INSERT_USER = "INSERT INTO Users (Username, Password, Email, Phone) VALUES (?, ?, ?, ?)";
	    public static final String LOGIN_USER = "SELECT Email, Password FROM Users WHERE Email = ?";
	    public static final String IS_USER_EXISTS = "SELECT 1 FROM Users WHERE Email = ? OR Phone = ?";
	    public static final String GET_USER_BY_ID = "SELECT * FROM Users WHERE UserID = ?";
	    public static final String UPDATE_USER = "UPDATE Users SET Username = ?, Password = ?, Email = ?, Phone = ? WHERE UserID = ?";
	    public static final String DELETE_USER = "DELETE FROM Users WHERE UserID = ?";
    	public static final String GET_USERID_BY_EMAIL = "SELECT UserID FROM Users WHERE Email = ?";
	}
	
	public static final class CategoryQuery{
		public static final String GET_ALL_CATEGORIES = "SELECT * FROM Categories";
		public static final String ADD_CATEGORY = "INSERT INTO UserCategories (CategoryName, UserID, IsDefault) VALUES (?, ?, ?)";
		public static final String ADD_CATEGORY_IN_USERS_CATEGORY = "INSERT INTO UserCategories (CategoryName, IsDefault) VALUES (?, ?)";
		public static final String UPDATE_CATEGORY = "UPDATE UserCategories SET CategoryName = ? WHERE CategoryID = ? AND UC.UserID = ?";
		public static final String DELETE_CATEGORY = "UPDATE UserCategories SET IsDefault = ? FROM Categories C INNER JOIN UserCategories UC ON C.CategoryID = UC.CategoryID WHERE UC.UserID = ?";
		public static final String GET_CATEGORIES_FOR_USER = "SELECT C.CategoryName, C.CategoryID, UC.IsDefault FROM Categories C INNER JOIN UserCategories UC ON C.CategoryID = UC.CategoryID WHERE UC.UserID = ? AND C.IsDefault= ?";
		public static final String GET_CATEGORIES_FOR_USER_TO_MANAGE = "SELECT C.CategoryName, C.CategoryID FROM Categories C INNER JOIN UserCategories UC ON C.CategoryID = UC.CategoryID WHERE UC.UserID = ? AND C.IsDefault= ? AND C.CategoryID != 1 AND C.CategoryID != 11";
		public static final String GET_CATEGORY_BY_ID = "SELECT C.CategoryID, C.CategoryName, C.IsDefault FROM Categories C INNER JOIN UserCategories UC ON C.CategoryID = UC.CategoryID WHERE C.CategoryID = ? AND C.IsDefault= ?";
	}
	
	public static final class ExpenseQuery{
		public static final String ADD_EXPENSE = "INSERT INTO Expenses (ExpenseName, UserID, Amount, Type, Payee, CategoryID, PaymentID, Status, ReferenceID, Description, Date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		public static final String GET_EXPENSE_BY_USER = "SELECT e.ExpenseID, e.ExpenseName, e.UserID, e.Amount, e.Type, e.Payee, e.CategoryID, c.CategoryName, e.PaymentID, p.PaymentName, e.Status, e.ReferenceID, e.Description, e.Date, e.CreatedAt, e.UpdatedAt FROM Expenses e JOIN Categories c ON e.CategoryID = c.CategoryID JOIN Mode_of_Payment p ON e.PaymentID = p.PaymentID WHERE e.UserID = ? AND e.status = ?";
		public static final String UPDATE_EXPENSE = "UPDATE Expenses SET ExpenseName = ?, Amount = ?, Type = ?, Payee = ?, CategoryID = ?, PaymentID = ?, Status = ?, ReferenceID = ?, Description = ?, Date = ?, UpdatedAt = CURRENT_TIMESTAMP WHERE ExpenseID = ?";
		public static final String IS_VALID_PAYMENTID = "SELECT 1 FROM Mode_of_Payment WHERE PAYMENTID = ?";
		public static final String GET_EXPENSE_BY_ID = "SELECT e.ExpenseID, e.ExpenseName, e.UserID, e.Amount, e.Type, e.Payee, e.CategoryID, c.CategoryName, e.PaymentID, p.paymentName, e.Status, e.ReferenceID, e.Description, e.Date, e.CreatedAt, e.UpdatedAt FROM Expenses e JOIN Categories c ON e.CategoryID = c.CategoryID JOIN MODE_OF_PAYMENT p ON e.PaymentID = p.PaymentID WHERE e.ExpenseID= ?";
		public static final String DELETE_EXPENSE = "UPDATE Expenses SET Status = ? WHERE ExpenseID = ?";
		public static final String GET_ALL_EXPENSES = "SELECT e.ExpenseID, e.ExpenseName, e.UserID, e.Amount, e.Type, e.Payee, e.CategoryID, c.CategoryName, e.PaymentID, p.paymentName, e.Status, e.ReferenceID, e.Description, e.Date, e.CreatedAt, e.UpdatedAt FROM Expenses e JOIN Categories c ON e.CategoryID = c.CategoryID JOIN MODE_OF_PAYMENT p ON e.PaymentID = p.PaymentID WHERE e.status = ? AND e.UserID = ?";
		public static final String CALCULATE_INCOME_SPENT_REMAINING = "SELECT e.Amount, e.Type FROM Expenses e JOIN Categories c ON e.CategoryID = c.CategoryID WHERE e.UserID = ? AND e.Status = ?";
		public static final String GET_DATE_RANGE_REPORT = "SELECT e.ExpenseID, e.ExpenseName, e.UserID, e.Amount, e.Payee, e.CategoryID, c.CategoryName, e.PaymentID, p.paymentName, e.Status, e.ReferenceID, e.Description, e.Date, e.CreatedAt, e.UpdatedAt FROM Expenses e JOIN Categories c ON e.CategoryID = c.CategoryID JOIN MODE_OF_PAYMENT p ON e.PaymentID = p.PaymentID WHERE e.UserID = ? AND Type= ? AND e.status = ? AND date BETWEEN ? AND ?";
		public static final String GET_CATEGORYWISE_REPORT = "SELECT e.categoryid, c.categoryname, SUM(e.amount) as amount FROM expenses e JOIN categories c ON e.categoryid = c.categoryID WHERE e.userid = ? AND status = ? AND e.Type = ? AND e.categoryid = ? GROUP BY e.categoryid";
		public static final String GET_USERID_BY_EXPENSEID = "SELECT UserID FROM Expenses WHERE expenseid = ?";
		public static final String GET_EXPENSES_BY_CATEGORY = "SELECT e.ExpenseID, e.ExpenseName, e.UserID, e.Amount, e.Type, e.Payee, e.CategoryID, c.CategoryName, e.PaymentID, p.paymentName, e.Status, e.ReferenceID, e.Description, e.Date, e.CreatedAt, e.UpdatedAt FROM Expenses e JOIN Categories c ON e.CategoryID = c.CategoryID JOIN MODE_OF_PAYMENT p ON e.PaymentID = p.PaymentID WHERE e.UserID = ? AND e.CategoryID = ? AND e.status = ?";
		public static final String GET_EXPENSES_BY_PAYMENTMODE = "SELECT e.ExpenseID, e.ExpenseName, e.UserID, e.Amount, e.payee, e.Type, e.CategoryID, c.CategoryName, e.PaymentID, p.paymentName, e.Status, e.ReferenceID, e.Description, e.Date, e.CreatedAt, e.UpdatedAt FROM Expenses e JOIN Categories c ON e.CategoryID = c.CategoryID JOIN MODE_OF_PAYMENT p ON e.PaymentID = p.PaymentID WHERE e.UserID = ? AND e.PaymentID = ? AND e.status = ?";
		public static final String GET_EXPENSES_BY_CATEGORY_AND_PAYMENTMODE = "SELECT e.ExpenseID, e.ExpenseName, e.UserID, e.Amount, e.Payee, e.Type, e.CategoryID, c.CategoryName, e.PaymentID, p.paymentName, e.Status, e.ReferenceID, e.Description, e.Date, e.CreatedAt, e.UpdatedAt FROM Expenses e JOIN Categories c ON e.CategoryID = c.CategoryID JOIN MODE_OF_PAYMENT p ON e.PaymentID = p.PaymentID WHERE e.UserID = ? AND e.PaymentID = ? AND e.status = ? AND e.CategoryID = ?";
	}
	
	public static final class PaymentModeQuery{
		public static final String GET_PAYMENTMODE_BY_ID = "SELECT * FROM MODE_OF_PAYMENT WHERE PaymentModeID = ?";
		public static final String GET_ALL_PAYMENTMODES = "SELECT * FROM MODE_OF_PAYMENT";
	}
	
	public static final class UserCategoryQuery{
	    public static final String ASSIGN_DEFAULT_CATEGORIES = "INSERT INTO UserCategories (UserID, CategoryID, IsDefault) SELECT ?, c.CategoryID, TRUE FROM Categories c WHERE c.IsDefault = ?";
	    public static final String ADD_USER_CATEGORY = "INSERT INTO UserCategories (UserID, CategoryID, IsDefault) VALUES (?, ?, ?)";
	    public static final String GET_CATEGORIES_FOR_USER = "SELECT c.* FROM Categories c INNER JOIN UserCategories uc ON c.CategoryID = uc.CategoryID WHERE uc.UserID = ? AND IsDefault = ?";
	    public static final String REMOVE_USER_CATEGORY = "UPDATE UserCategories SET IsDefault = ? WHERE UserCategoryID = ?";
	}
	
	public static final class UserTable{
		public static final String USERID = "userid";
		public static final String USERNAME = "username";
		public static final String PASSWORD = "password";
		public static final String EMAIL = "email";
		public static final String PHONE = "phone";
		public static final String CREATEDAT = "createdat";
	}

	public static final class CategoriesTable{
		public static final String CATEGORYID = "categoryid";
		public static final String CATEGORYNAME = "categoryname";
		public static final String ISDEFAULT = "isdefault";
		public static final String CREATEDAT = "createdat";
	}
	
	public static final class ExpensesTable{
		public static final String EXPENSEID = "expenseid";
		public static final String EXPENSENAME = "expensename";
		public static final String USERID = "userid";  	
		public static final String AMOUNT = "amount";
		public static final String PAYEE = "payee"; 
		public static final String CATEGORYID = "categoryid"; 
		public static final String PAYMENTID = "paymentid";
		public static final String STATUS = "status";
		public static final String REFERENCEID = "referenceid";
		public static final String DESCRIPTION = "description";
		public static final String DATE = "date";
		public static final String CREATEDAT = "createdat"; 
		public static final String UPDATEDAT = "updatedat";
		public static final String TYPE = "type";
	}
	
	public static final class ModeOfPaymentTable{
		public static final String PAYMENTID = "paymentid";
		public static final String PAYMENTNAME = "paymentname";
		public static final String CREATEDAT = "createdat";
	}
	
	public static final class UserCategoriesTable{
		public static final String USERCATEGORYID = "usercategoryid";
		public static final String USERID = "userid";
		public static final String CATEGORYID = "categoryid"; 	
		public static final String ISDEFAULT = "isdefault";	
		public static final String CREATEDAT = "createdat"; 	
		public static final String STATUS = "status";	
		public static final String UPDATEDAT = "updatedat";  
	}
	
	public static final class Tables{
		public static final String USERS = "users";
		public static final String EXPENSES = "expenses";
		public static final String USERCATEGORIES = "usercategories";
		public static final String MODE_OF_PAYMENT = "mode_of_payment";
		public static final String CATEGORIES = "categories";
	}
	
	public static final class JspPages{
		public static final String ADD_CATEGORY = "addCategory.jsp";
		public static final String ADD_EXPENSE = "addExpense.jsp";
		public static final String DASHBOARD = "dashboard.jsp";
		public static final String DELETE_CATEGORY = "deleteCategory.jsp";
		public static final String DELETE_EXPENSE = "deleteExpense.jsp";
		public static final String EDIT_CATEGORY = "editCategory.jsp";
		public static final String EDIT_EXPENSE = "editExpense.jsp";
		public static final String ERROR = "error.jsp";
		public static final String EXPESNSE_FORM = "expenseForm.jsp";
		public static final String EXPENSE_REPORT_FORM = "expenseReportForm.jsp";
		public static final String EXPENSE_REPORT_RESULT = "expenseReportResult.jsp";
		public static final String LOGIN = "login.jsp";
		public static final String MANAGE_CATEGORY = "manageCategory.jsp";
		public static final String NAVIGATION = "navigation.jsp";
		public static final String REGISTER = "register.jsp";
		public static final String SUCCESS_EDIT = "successEdit.jsp";
		public static final String VIEW_EXPENSE = "viewExpense.jsp";
	}
	
	public static final class GetParameters{
		public static final String EMAIL = "email";
		public static final String PASSWORD = "password";
		public static final String USERNAME = "username";
		public static final String PHONE = "PHONE";
		public static final String CATEGORY_FILTER = "categoryFilter";
		public static final String PAYMENT_MODE_FILTER = "paymentModeFilter";
		public static final String ACTION = "action";
		public static final String EXPENSEID = "expenseId";
		public static final String EXPENSENAME = "expenseName";
		public static final String AMOUNT = "amount";
		public static final String PAYEE = "payee";
		public static final String TYPE = "type";
		public static final String CATEGORYID = "categoryID";
		public static final String PAYMENTID = "paymentID";
		public static final String REFERENCEID = "referenceID";
		public static final String DESCRIPTION = "description";
		public static final String DATE = "date";
		public static final String ExpenseID = "expenseID";
		public static final String CATEGORYNAME = "categoryName";
		public static final String REPORTTYPE = "reportType";
		public static final String STARTDATE = "startDate";
		public static final String ENDDATE = "endDate";
		public static final String CATEGORY = "category";
		public static final String DATERANGE = "dateRange";
	}
	
	public static final class SetAttributes{
		public static final String EMAIL = "email";
		public static final String PASSWORD = "password";
		public static final String USERNAME = "username";
		public static final String CATEGORIES = "categories";
		public static final String PAYMENTMODES = "paymentModes";
		public static final String INCOME = "income";
		public static final String REMAINING = "remaining";
		public static final String SPENT = "spent";
		public static final String EXPENSES = "expenses";
		public static final String ERROR_MESSAGE = "errorMessage";
		public static final String SUCCESS_MESSAGE = "successMessage";
		public static final String EXPENSE = "expense";
		public static final String CATEGORY = "category";
		public static final String EXPENSELIST = "expenseList";
		public static final String TOTAL = "total";
		public static final String AVERAGE = "average";
		public static final String HIGHEST = "highest";
		public static final String LOWEST = "lowest";
		public static final String REPORTTYPE = "reportType";
		public static final String STARTDATE = "startDate";
		public static final String ENDDATE = "endDate";
	}
	
	public static final class LogMessage{
		public static final class Warn{
			public static final String M1 = "Unauthorized access attempt detected. Redirecting to login.";
			public static final String M2 = "Invalid filter parameters: ";
			public static final String M3 = "User already exists: ";
			public static final String M4 = "Expense not found for ID: {}";
			public static final String M5 = "Expense ID is missing.";
			public static final String M6 = "Invalid payment ID provided: {}";
			public static final String M7 = "Attempt to register user failed: User already exists with Email: {} or Phone: {}";
			public static final String M8 = "User not found with ID: {}";
			public static final String M9 = "User not found error";
			public static final String M10 = "User not found with email: {}";
			public static final String M11 = "Transaction rolled back at ";
		}
		
		public static final class Info{
			public static final String M1 = "Expenses and additional attributes set. Forwarding to dashboard.jsp.";
			public static final String M2 = "Received POST request with action: {}";
			public static final String M3 = "Received GET request with action: {}";
			public static final String M4 = "Expense added successfully for userID: {}";
			public static final String M5 = "User successfully registered: {}";
			public static final String M6 = "User successfully deleted with ID: {}";
			public static final String M7 = "User successfully logged in: {}";
			public static final String M8 = "User exists with Email: {} or Phone: {}";
			public static final String M9 = "User successfully updated: {}";
			public static final String M10 = "PaymentID validation result: {} for PaymentID: {}";
			public static final String M11 = "Expense updated successfully for ExpenseID: {}";
		}
		
		public static final class Error{
			public static final String M1 = "General exception during registration: ";
			public static final String M2 = "SQL Exception during registration process: ";
			public static final String M3 = "Unexpected exception during registration: ";
			public static final String M4 = "Parameter validation failed: ";
			public static final String M5 = "Null pointer exception: ";
			public static final String M6 = "Error occurred while processing login request: ";
			public static final String M7 = "Invalid action received: {}";
			public static final String M8 = "User session expired. Please log in again.";
			public static final String M9 = "Unhandled exception occurred while processing action: {}";
			public static final String M10 = "Unhandled exception occurred while processing Expense";
			public static final String M11 = "Error while fetching expense ID.";
			public static final String M12 = "Invalid or missing expense ID.";
			public static final String M13 = "Failed to add expense for userID: {}";
			public static final String M14 = "Error while adding expense";
			public static final String M15 = "Failed to update expense for ID: {}";
			public static final String M16 = "Error while editing expense";
			public static final String M17 = "Failed to delete the expense.";
			public static final String M18 = "Error occurred while deleting expense";
			public static final String M19 = "Error while fetching category ID.";
			public static final String M20 = "Error closing resources";
			public static final String M21 = "Unexpected error during deleting user";
			public static final String M22 = "SQL Exception during deleting user";
			public static final String M23 = "SQL Exception during user registration";
			public static final String M24 = "Unexpected error during user registration";
			public static final String M25 = "SQL Exception during user login";
			public static final String M26 = "Unexpected error during user login";
			public static final String M27 = "SQL Exception during checking if user exists";
			public static final String M28 = "Unexpected error during user existence check";
			public static final String M29 = "SQL Exception during fetching user by ID";
			public static final String M30 = "Unexpected error while fetching user by ID";
			public static final String M31 = "Rollback error during user update";
			public static final String M32 = "SQL Exception during updating user";
			public static final String M33 = "Unexpected error during updating user";
			public static final String M34 = "SQL Exception during fetching user ID by email";
			public static final String M35 = "Unexpected error during user ID fetch by email";
			public static final String M36 = "Unknown Exception Occured at CategoryDAO.getAllCategories: {}";
			public static final String M37 = "Unknown Exception Occured at CategoryDAO.addCategory: {}";
			public static final String M38 = "Unknown Exception Occured at CategoryDAO.updateCategory: {}";
			public static final String M39 = "Unknown Exception Occured at CategoryDAO.deleteCategory: {}";
			public static final String M40 = "Unknown Exception Occured at CategoryDAO.getCategoriesForUser: {}";
			public static final String M41 = "Unknown Exception Occured at CategoryDAO.getCategoriesForUserToManage: {}";
			public static final String M42 = "SQL Exception while retreiving Category with CategoryID: {}, Error: {}";
			public static final String M43 = "Unexpected error while retreiving Category with CategoryID: {}, Error: {}";
			public static final String M44 = "Unexpected error while retrieving PaymentModes at PaymentModeDAO.getAllPaymentModes, Error: {}";
			public static final String M45 = "Unexpected error while retrieving PaymentMode at PaymentModeDAO.getPaymentModeByID, Error: {}";
			public static final String M46 = "Unexpected error while adding category at UserCategoryDAO.addUserCategory, Error: {}";
			public static final String M47 = "Unexpected error while retrieving categories at UserCategoryDAO.getCategoriesForUser, Error: {}";
			public static final String M48 = "Unexpected error while removing category at UserCategoryDAO.removeUserCategory, Error: {}";
			public static final String M49 = "Unexpected error while assigning default categories at UserCategoryDAO.assignDefaultCategories, Error: {}";
			public static final String M50 = "Error during rollback: {}";
			public static final String M51 = "Unexpected error while Calculating Income, Spent and Remaining at ExpenseDAO.calculateIncomeSpentRemaining, Error: {}";
			public static final String M52 = "Unexpected error while calculating Date Range report details at ExpenseDAO.getDateRangeReport, Error: {}";
			public static final String M53 = "SQL Exception while adding expense: {}, Error: {}";
			public static final String M54 = "Null value encountered while adding expense: {}, Error: {}";
			public static final String M55 = "Unexpected error while adding expense: {}, Error: {}";
			public static final String M56 = "SQL Exception during payment ID validation for ID: {}, Error: {}";
			public static final String M57 = "Unexpected error during payment ID validation for ID: {}, Error: {}";
			public static final String M58 = "Unexpected error while getting Expenses with User details: {}, Error: {}";
			public static final String M59 = "SQL Exception while updating expense: {}, Error: {}";
			public static final String M60 = "Unexpected error while updating expense: {}, Error: {}";
			public static final String M61 = "SQL Exception while retreiving expense with ExpenseID: {}, Error: {}";
			public static final String M62 = "Unexpected error while retreiving expense with ExpenseID: {} at ExpenseDAO.getExpenseByID, Error: {}";
			public static final String M63 = "SQL Exception while deleting expense with ExpenseID: {}, Error: {}";
			public static final String M64 = "Unexpected error while deleting expense with ExpenseID: {} at ExpenseDAO.deleteExpense, Error: {}";
			public static final String M65 = "Unexpected error while retrieving expenses at ExpenseDAO.getAllExpenses, Error: {}";
			public static final String M66 = "Unexpected error while retrieving expenses at ExpenseDAO.getExpensesByCategory, Error: {}";
			public static final String M67 = "Unexpected error while retrieving expenses at ExpenseDAO.getExpensesByPaymentMode, Error: {}";
			public static final String M68 = "Unexpected error while retrieving expenses at ExpenseDAO.getExpensesByCategoryAndPaymentMode, Error: {}";
			public static final String M69 = "Error fetching user by Email";
			public static final String M70 = "Unexpected error while calculating Categorywise report details at ExpenseDAO.getCategoryWiseReport, Error: {}";
		}
	}
	
	public static final class ExceptionMessage{
		public static final class InvalidFilterException{
			public static final String M1 = "Filter parameters must be integers.";
			public static final String M2 = "Category filter must be a valid integer.";
			public static final String M3 = "Payment Mode filter must be a valid integer.";
		}
		
		public static final class UserAlreadyExistsException{
			public static final String M1 = "Email or phone number already exists.";
			public static final String M2 = "User already exists with the provided email or phone";
		}
		
		public static final class NullPointerException{
			public static final String M1 = "User is null after querying from DB";
		}
		
		public static final class IllegalArgumentException{
			public static final String M1 = "Email or Password is missing";
		}
		public static final class InvalidReportParameterException{
			public static final String M1 = "Report type is required.";
			public static final String M2 = "Invalid category ID provided.";
			public static final String M3 = "Start and end dates are required for date range reports.";
			public static final String M4 = "Invalid report type specified.";
		}
		public static final class DatabaseConnectionException{
			public static final String M1 = "Unexpected error during user deletion";
			public static final String M2 = "Unexpected error during user registration";
			public static final String M3 = "Unexpected error during user login";
			public static final String M4 = "Unexpected error during user existence check";
			public static final String M5 = "Unexpected error while fetching user by ID";
			public static final String M6 = "Unexpected error during updating user";
			public static final String M7 = "Unexpected error during user ID fetch by email";
		}
		public static final class QueryExecutionException{
			public static final String M1 = "Error executing delete user query";
			public static final String M2 = "Error executing query in registerUser";
			public static final String M3 = "Error executing login query";
			public static final String M4 = "Error executing query to check if user exists";
			public static final String M5 = "Error executing query to fetch user by ID";
			public static final String M6 = "Error executing update user query";
			public static final String M7 = "Error executing query to fetch user ID by email";
		}
		public static final class UserNotFoundException{
			public static final String M1 = "User not found with email: ";
		}
	}
	
	public static final class Error{
		public static final String E1 = "Database error occurred: ";
		public static final String E2 = "Unexpected error: ";
		public static final String E3 = "Unexpected error occurred during report generation: ";
		public static final String E4 = "Invalid parameters provided for report generation: ";
		public static final String E5 = "Error parsing number values, like category ID.";

	}
	
	public static final class ErrorMessage{
		public static final String E1 = "Database error occurred. Please try again later.";
		public static final String E2 = "An unexpected error occurred. Please try again later.";
		public static final String E3 = "Invalid filter parameters.";
		public static final String E4 = "Registration failed, please try again.";
		public static final String E5 = "An error occurred during registration. Please try again.";
		public static final String E6 = "Email or phone number already exists. Please use a different one.";
		public static final String E7 = "An error occurred during registration. Please try again.";
		public static final String E8 = "Email or Password is missing";
		public static final String E9 = "Invalid email or password. Please try again.";
		public static final String E10 = "An error occurred while processing your request. Please try again later.";
		public static final String E11 = "Invalid input: ";
		public static final String E12 = "Invalid action!";
		public static final String E13 = "Expense not found.";
		public static final String E14 =  "Missing expense ID.";
		public static final String E15 =  "Invalid expense ID.";
		public static final String E16 =  "Invalid PaymentID.";
		public static final String E17 = "Failed to add expense.";
		public static final String E18 = "Invalid data! Please check inputs.";
		public static final String E19 = "Failed to update expense.";
		public static final String E20 =  "User session expired. Please log in again.";
		public static final String E21 = "Failed to delete the expense.";
		public static final String E22 = "Invalid or missing expense ID.";
		public static final String E23 = "Failed to add category.";
		public static final String E24 = "Invalid category ID.";
		public static final String E25 = "Failed to delete category.";
		public static final String E26 = "Failed to update category.";
		public static final String E27 = "Invalid number format for category ID.";
		public static final String E28 = "An error occurred while generating the report.";
		public static final String E29 = "Unknown Exception Occurred at DBConnection.getConnection.";
		public static final String E30 = "Database driver class not found at DBConnection.getConnection.";
	}
	
	public static final class Util{
		public static final String COMMA = ", ";
		public static final String DOT = ".";
	}
	
	public static final class Servlets{
		public static final String REGISTER = "/register";
		public static final String DASHBOARD = "/dashboard";
		public static final String LOGIN = "/login";
		public static final String EXPENSE = "/expense";
		public static final String CATEGORY = "/category";
		public static final String EXPENSEREPORTSERVLET = "/ExpenseReportServlet";
		public static final String LOGOUT = "/logout";
	}
	
	public static final class Action{
		public static final String ADD = "add";
		public static final String DELETE = "delete";
		public static final String VIEW = "view";
		public static final String UPDATE = "update";
		public static final String EDIT = "edit";
	}
	
	public static final class Values{
		public static final String ACTIVE = "Active";
		public static final String INACTIVE = "Inactive";
		public static final String SPENT = "spent";
		public static final String EXPENSELIST = "expenseList";
		public static final String TOTAL = "total";
		public static final String HIGHEST = "highest";
		public static final String LOWEST = "lowest";
		public static final String AVERAGE = "average";
		public static final String INCOME = "Income";
	}
	
	public static final class SuccessMessage{
		public static final String M1 = "Expense added successfully!";
		public static final String M2 = "Expense updated successfully.";
		public static final String M3 = "Expense deleted successfully.";
	}
	
	public static final class ClassMethod{
		public static final String M1 = "DBConnection.getConnection";
		public static final String M2 = "UserDAO.registerUser.rollback";
		public static final String M3 = "CategoryDAO.getAllCategories";
		public static final String M4 = "CategoryDAO.addCategory";
		public static final String M5 = "CategoryDAO.updateCategory";
		public static final String M6 = "CategoryDAO.deleteCategory";
		public static final String M7 = "CategoryDAO.getCategoriesForUser";
		public static final String M8 = "CategoryDAO.getCategoriesForUser";
		public static final String M9 = "CategoryDAO.getCategoryByID";
		public static final String M10 = "PaymentModeDAO.getAllPaymentModes";
		public static final String M11 = "PaymentModeDAO.getPaymentModeByID";
		public static final String M12 = "UserCategoryDAO.addUserCategory";
		public static final String M13 = "UserCategoryDAO.getCategoriesForUser";
		public static final String M14 = "UserCategoryDAO.removeUserCategory";
		public static final String M15 = "UserCategoryDAO.assignDefaultCategories";
		public static final String M16 = "ExpenseDAO.addExpense";
		public static final String M17 = "ExpenseDAO.getExpensesByUser";
		public static final String M18 = "ExpenseDAO.updateExpense.rollback";
		public static final String M19 = "ExpenseDAO.updateExpense";
		public static final String M20 = "ExpenseDAO.getExpenseByID";
		public static final String M21 = "ExpenseDAO.deleteExpense.rollback";
		public static final String M22 = "ExpenseDAO.deleteExpense";
		public static final String M23 = "ExpenseDAO.getAllExpenses";
		public static final String M24 = "ExpenseDAO.getExpensesByCategory";
		public static final String M25 = "ExpenseDAO.getExpensesByPaymentMode";
		public static final String M26 = "ExpenseDAO.getExpensesByCategoryAndPaymentMode";
		public static final String M27 = "ExpenseDAO.getCategoryWiseReport";
		public static final String M28 = "ExpenseDAO.getDateRangeReport";
		public static final String M29 = "ExpenseDAO.calculateIncomeSpentRemaining";
	}
}
