package com.expensetracker.servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;

import com.expensetracker.dao.CategoryDAO;
import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.dao.PaymentModeDAO;
import com.expensetracker.dao.UserDAO;
import com.expensetracker.exceptions.DatabaseConnectionException;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.model.PaymentMode;
import com.expensetracker.util.Constants;

import java.util.List;

@WebServlet(Constants.Servlets.EXPENSE)
public class ExpenseServlet extends HttpServlet {
	private static final Logger LOGGER = LogManager.getLogger(ExpenseServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//Remove cache
    	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache"); 
        response.setDateHeader("Expires", 0); 
        
		
		String action = request.getParameter(Constants.GetParameters.ACTION);
		LOGGER.info(Constants.LogMessage.Info.M2, action);
		try {
			switch (action) {
			case Constants.Action.ADD :
				addExpense(request, response);
				break;
			case Constants.Action.EDIT:
				editExpense(request, response);
				break;
			case Constants.Action.DELETE:
				deleteExpense(request, response);
				break;
			case Constants.Action.VIEW:
				viewExpense(request, response);
				break;
			default:
				LOGGER.error(Constants.LogMessage.Error.M7, action);
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, Constants.ErrorMessage.E12);
				break;
			}
		} catch (DatabaseConnectionException e) {
            LOGGER.error(Constants.Error.E1+ e.getMessage());
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E1);
            request.getRequestDispatcher(Constants.JspPages.ERROR).forward(request, response);
        }catch (Exception e) {
			LOGGER.error(Constants.LogMessage.Error.M9, action, e);
			response.sendRedirect(Constants.JspPages.ERROR);
		}

	}

	private void viewExpense(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String expenseID = request.getParameter(Constants.GetParameters.ExpenseID);
		if (expenseID != null && !expenseID.isEmpty()) {
			try {
				int id = Integer.parseInt(expenseID);
				ExpenseDAO expenseDAO = new ExpenseDAO();
				Expense expense = expenseDAO.getExpenseByID(id);
				request.getSession().setAttribute(Constants.SetAttributes.EXPENSE, expense);
				response.sendRedirect(Constants.JspPages.VIEW_EXPENSE);
			} catch (Exception e) {
				LOGGER.error(Constants.LogMessage.Error.M10);
				response.sendRedirect(Constants.JspPages.ERROR);
			}
		} else {
			LOGGER.warn(Constants.LogMessage.Warn.M5);
			request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE,Constants.ErrorMessage.E14);
			request.getRequestDispatcher(Constants.JspPages.DASHBOARD).forward(request, response);
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//Remove cache
    	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache"); 
        response.setDateHeader("Expires", 0); 
        
		String action = request.getParameter(Constants.GetParameters.ACTION);
		LOGGER.info(Constants.LogMessage.Info.M3, action);
		try {
			switch (action) {
			case Constants.Action.EDIT:
				getEditExpense(request, response);
				break;
			case Constants.Action.VIEW:
				viewExpense(request, response);
				break;
			default:
				LOGGER.error(Constants.LogMessage.Error.M7, action);
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, Constants.ErrorMessage.E12);
				break;
			}
		} catch (DatabaseConnectionException e) {
            LOGGER.error(Constants.Error.E1+ e.getMessage());
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E1);
            request.getRequestDispatcher(Constants.JspPages.ERROR).forward(request, response);
        }catch (Exception e) {
			LOGGER.error(Constants.LogMessage.Error.M9, action, e);
			response.sendRedirect(Constants.JspPages.ERROR);
		}

	}

	private void getEditExpense(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String expenseID = request.getParameter(Constants.GetParameters.ExpenseID);
		if (expenseID != null && !expenseID.isEmpty()) {
			try {
				int id = Integer.parseInt(expenseID);
				ExpenseDAO expenseDAO = new ExpenseDAO();
				Expense expense = expenseDAO.getExpenseByID(id);

				if (expense != null) {
					CategoryDAO categoryDAO = new CategoryDAO();
					PaymentModeDAO paymentModeDAO = new PaymentModeDAO();

					List<Category> categories = categoryDAO.getCategoriesForUser(expense.getUserID());
					List<PaymentMode> paymentModes = paymentModeDAO.getAllPaymentModes();

					request.setAttribute(Constants.SetAttributes.EXPENSE, expense);
					request.setAttribute(Constants.SetAttributes.CATEGORIES, categories);
					request.setAttribute(Constants.SetAttributes.PAYMENTMODES, paymentModes);

					RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.EDIT_EXPENSE);
					dispatcher.forward(request, response);
				} else {
					LOGGER.warn(Constants.LogMessage.Warn.M4, expenseID);
					request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E13);
					request.getRequestDispatcher(Constants.JspPages.DASHBOARD).forward(request, response);
				}
			} catch (DatabaseConnectionException e) {
	            LOGGER.error(Constants.Error.E1 + e.getMessage());
	            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E1);
	            request.getRequestDispatcher(Constants.JspPages.ERROR).forward(request, response);
	        }catch (NumberFormatException e) {
				LOGGER.error(Constants.LogMessage.Error.M11, e);
				request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E15);
				request.getRequestDispatcher(Constants.JspPages.ERROR).forward(request, response);
			}

		} else {
			LOGGER.warn(Constants.LogMessage.Warn.M5);
			request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E14);
			request.getRequestDispatcher(Constants.JspPages.DASHBOARD).forward(request, response);
		}

	}

	private void addExpense(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			String expenseName = request.getParameter(Constants.GetParameters.EXPENSENAME);
			double amount = Double.parseDouble(request.getParameter(Constants.GetParameters.AMOUNT));
			int categoryID = Integer.parseInt(request.getParameter(Constants.GetParameters.CATEGORYID));
			String type = request.getParameter(Constants.GetParameters.TYPE);
			String payee = request.getParameter(Constants.GetParameters.PAYEE);
			String referenceID = request.getParameter(Constants.GetParameters.REFERENCEID);
			String description = request.getParameter(Constants.GetParameters.DESCRIPTION);
			int paymentID = Integer.parseInt(request.getParameter(Constants.GetParameters.PAYMENTID));

			java.sql.Date date = java.sql.Date.valueOf(request.getParameter(Constants.GetParameters.DATE));

			HttpSession session = request.getSession();
			String email = (String) session.getAttribute(Constants.GetParameters.EMAIL);
			UserDAO userDAO = new UserDAO();
			int userID = userDAO.getUserIDByEmail(email);

			Expense expense = new Expense();
			expense.setExpenseName(expenseName);
			expense.setAmount(amount);
			expense.setCategoryID(categoryID);
			expense.setType(type);
			expense.setPayee(payee);
			expense.setReferenceID(referenceID);
			expense.setDescription(description);
			expense.setDate(date);
			expense.setUserID(userID);
			expense.setPaymentModeID(paymentID);

			ExpenseDAO expenseDAO = new ExpenseDAO();

			if (!expenseDAO.isValidPaymentID(paymentID)) {
				LOGGER.warn(Constants.LogMessage.Warn.M6, paymentID);
				request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E16);
				RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.ADD_EXPENSE);
				dispatcher.forward(request, response);
				return;
			}
			boolean isAdded = expenseDAO.addExpense(expense);

			if (isAdded) {
				List<Expense> updatedExpenses = expenseDAO.getExpensesByUser(userID);
				session.setAttribute(Constants.SetAttributes.EXPENSES, updatedExpenses);
				double[] result = expenseDAO.calculateIncomeSpentRemaining(userID);
				
				session.setAttribute(Constants.SetAttributes.INCOME, result[0]);
				session.setAttribute(Constants.SetAttributes.SPENT, result[1]);
				session.setAttribute(Constants.SetAttributes.REMAINING, result[2]);
				LOGGER.info(Constants.LogMessage.Info.M4, userID);
				request.setAttribute(Constants.SetAttributes.SUCCESS_MESSAGE, Constants.SuccessMessage.M1);
				RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.DASHBOARD);
				dispatcher.forward(request, response);
			} else {
				LOGGER.error(Constants.LogMessage.Error.M13, userID);
				request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E17);
				request.getRequestDispatcher(Constants.JspPages.ERROR).forward(request, response);
			}

		} catch (DatabaseConnectionException e) {
            LOGGER.error(Constants.Error.E1 + e.getMessage());
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E1);
            request.getRequestDispatcher(Constants.JspPages.ERROR).forward(request, response);
        }catch (Exception e) {
			LOGGER.error(Constants.LogMessage.Error.M14, e);
			request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E18);
			RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.ERROR);
			dispatcher.forward(request, response);
		}
	}

	protected void editExpense(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String expenseID = request.getParameter(Constants.GetParameters.EXPENSEID);
		if (expenseID != null && !expenseID.isEmpty()) {
			try {
				int id = Integer.parseInt(expenseID);
				String expenseName = request.getParameter(Constants.GetParameters.EXPENSENAME);
				double amount = Double.parseDouble(request.getParameter(Constants.GetParameters.AMOUNT));
				String payee = request.getParameter(Constants.GetParameters.PAYEE);
				String type = request.getParameter(Constants.GetParameters.TYPE);
				int categoryID = Integer.parseInt(request.getParameter(Constants.GetParameters.CATEGORYID));
				int paymentID = Integer.parseInt(request.getParameter(Constants.GetParameters.PAYMENTID));
				String referenceID = request.getParameter(Constants.GetParameters.REFERENCEID);
				String description = request.getParameter(Constants.GetParameters.DESCRIPTION);
				java.sql.Date date = java.sql.Date.valueOf(request.getParameter(Constants.GetParameters.DATE));

				HttpSession session = request.getSession();
				String email = (String) session.getAttribute(Constants.GetParameters.EMAIL);
				UserDAO userDAO = new UserDAO();
				int userID = userDAO.getUserIDByEmail(email);
				ExpenseDAO expenseDAO = new ExpenseDAO();
				Expense expense = expenseDAO.getExpenseByID(id);

				System.out.print(expense);

				expense.setExpenseID(id);
				expense.setExpenseName(expenseName);
				expense.setAmount(amount);
				expense.setCategoryID(categoryID);
				expense.setType(type);
				expense.setStatus(Constants.Values.ACTIVE);
				expense.setPayee(payee);
				expense.setReferenceID(referenceID);
				expense.setDescription(description);
				expense.setDate(date);
				expense.setUserID(userID);
				expense.setPaymentModeID(paymentID);

				boolean isUpdated = expenseDAO.updateExpense(expense);
				System.out.print(expense);

				if (isUpdated) {
					List<Expense> updatedExpenses = expenseDAO.getExpensesByUser(userID);
					session.setAttribute(Constants.SetAttributes.EXPENSES, updatedExpenses);

					double[] result = expenseDAO.calculateIncomeSpentRemaining(userID);
					session.setAttribute(Constants.SetAttributes.INCOME, result[0]);
					session.setAttribute(Constants.SetAttributes.SPENT, result[1]);
					session.setAttribute(Constants.SetAttributes.REMAINING, result[2]);

					request.setAttribute(Constants.SetAttributes.SUCCESS_MESSAGE,Constants.SuccessMessage.M2);

					response.sendRedirect(Constants.JspPages.SUCCESS_EDIT);
				} else {
					LOGGER.error(Constants.LogMessage.Error.M15, expenseID);
					request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E19);
					RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.ERROR);
					dispatcher.forward(request, response);
				}
			} catch (DatabaseConnectionException e) {
	            LOGGER.error(Constants.Error.E1+ e.getMessage());
	            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E1);
	            request.getRequestDispatcher(Constants.JspPages.ERROR).forward(request, response);
	        }catch (Exception e) {
				e.printStackTrace();
				LOGGER.error(Constants.LogMessage.Error.M16, e);
				request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E18);
				RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.ERROR);
				dispatcher.forward(request, response);
			}
		}
	}

	private void deleteExpense(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			String expenseIDParam = request.getParameter(Constants.GetParameters.ExpenseID);
			if (expenseIDParam != null && !expenseIDParam.isEmpty()) {
				int expenseID = Integer.parseInt(expenseIDParam);

				HttpSession session = request.getSession();
				String email = (String) session.getAttribute(Constants.GetParameters.EMAIL);
				UserDAO userDAO = new UserDAO();
				int userID = userDAO.getUserIDByEmail(email);

				ExpenseDAO expenseDAO = new ExpenseDAO();
				Expense expense = expenseDAO.getExpenseByID(expenseID);

				if (expense == null) {
					LOGGER.warn(Constants.LogMessage.Warn.M4, expenseID);
					request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E13);
					response.sendRedirect(Constants.JspPages.ERROR);
					return;
				}

				request.setAttribute(Constants.SetAttributes.EXPENSE, expense);
				boolean isDeleted = expenseDAO.deleteExpense(expenseID);

				if (isDeleted) {
					if (email != null) {
						List<Expense> updatedExpenses = expenseDAO.getExpensesByUser(userID);
						session.setAttribute(Constants.SetAttributes.EXPENSES, updatedExpenses);
						double[] result = expenseDAO.calculateIncomeSpentRemaining(userID);
						session.setAttribute(Constants.SetAttributes.INCOME, result[0]);
						session.setAttribute(Constants.SetAttributes.SPENT, result[1]);
						session.setAttribute(Constants.SetAttributes.REMAINING, result[2]);

						request.setAttribute(Constants.SetAttributes.SUCCESS_MESSAGE,Constants.SuccessMessage.M3);

						response.sendRedirect(Constants.JspPages.DELETE_EXPENSE);
					} else {
						LOGGER.error(Constants.LogMessage.Error.M8);
						request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E20);
						response.sendRedirect(Constants.JspPages.LOGIN);
					}
				} else {
					LOGGER.error(Constants.LogMessage.Error.M17);
					request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E21);
					RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.ERROR);
					dispatcher.forward(request, response);
				}

			} else {
				LOGGER.error(Constants.LogMessage.Error.M12);
				request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E22);
				RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.ERROR);
				dispatcher.forward(request, response);
			}
		} catch (DatabaseConnectionException e) {
            LOGGER.error(Constants.Error.E1 + e.getMessage());
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E1);
            request.getRequestDispatcher(Constants.JspPages.ERROR).forward(request, response);
        }catch (Exception e) {
			LOGGER.error(Constants.LogMessage.Error.M18, e);
			request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E10);
			RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.ERROR);
			dispatcher.forward(request, response);
		}
	}

}
