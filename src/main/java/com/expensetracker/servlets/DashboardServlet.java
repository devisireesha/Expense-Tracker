package com.expensetracker.servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.expensetracker.dao.CategoryDAO;
import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.dao.PaymentModeDAO;
import com.expensetracker.dao.UserDAO;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.model.PaymentMode;
import com.expensetracker.util.Constants;
import com.expensetracker.exceptions.InvalidFilterException;
import com.expensetracker.exceptions.DatabaseConnectionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import java.util.List;

@WebServlet(Constants.Servlets.DASHBOARD)
public class DashboardServlet extends HttpServlet {
    
    private static final Logger LOGGER = LogManager.getLogger(DashboardServlet.class);
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//Remove cache
    	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache"); 
        response.setDateHeader("Expires", 0); 
        
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute(Constants.GetParameters.EMAIL);
        String username = (String) session.getAttribute(Constants.GetParameters.USERNAME);
        
        try {
            if (email == null) {
                LOGGER.warn(Constants.LogMessage.Warn.M1);
                response.sendRedirect(Constants.JspPages.LOGIN);
                return;
            }

            UserDAO userDAO = new UserDAO();
            int userID = userDAO.getUserIDByEmail(email);

            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> categories = categoryDAO.getCategoriesForUser(userID);
//            List<Category> categoriesToManage = categoryDAO.getCategoriesForUserToManage(userID);

            session.setAttribute(Constants.SetAttributes.CATEGORIES, categories); 
            request.setAttribute(Constants.SetAttributes.CATEGORIES, categories);
//            session.setAttribute("categoriesToManage", categoriesToManage);

            PaymentModeDAO paymentModeDAO = new PaymentModeDAO();
            List<PaymentMode> paymentmodes = paymentModeDAO.getAllPaymentModes();
            session.setAttribute(Constants.SetAttributes.PAYMENTMODES, paymentmodes); 
            request.setAttribute(Constants.SetAttributes.PAYMENTMODES, paymentmodes);

            ExpenseDAO expenseDAO = new ExpenseDAO();
            double[] result = expenseDAO.calculateIncomeSpentRemaining(userID);
            double income = result[0];
            double spent = result[1];
            double remaining = result[2];

            session.setAttribute(Constants.SetAttributes.INCOME, income);
            session.setAttribute(Constants.SetAttributes.SPENT, spent);
            session.setAttribute(Constants.SetAttributes.REMAINING, remaining);

            LOGGER.info(Constants.SetAttributes.INCOME + income + Constants.Util.COMMA +Constants.SetAttributes.SPENT + spent + Constants.Util.COMMA + Constants.SetAttributes.REMAINING + remaining);
            
            List<Expense> expenses = expenseDAO.getExpensesByUser(userID);
            String categoryFilter = request.getParameter(Constants.GetParameters.CATEGORY_FILTER);
            String paymentModeFilter = request.getParameter(Constants.GetParameters.PAYMENT_MODE_FILTER);

            try {
                if ((categoryFilter != null && !categoryFilter.isEmpty()) 
                        && (paymentModeFilter != null && !paymentModeFilter.isEmpty())) {
                    
                    expenses = applyMultipleFilters(expenses, categoryFilter, paymentModeFilter, userID, expenseDAO);
                } else if (categoryFilter != null && !categoryFilter.isEmpty()) {
                    expenses = applyCategoryFilter(expenses, categoryFilter, userID, expenseDAO);
                } else if (paymentModeFilter != null && !paymentModeFilter.isEmpty()) {
                    expenses = applyPaymentModeFilter(expenses, paymentModeFilter, userID, expenseDAO);
                }
            } catch (InvalidFilterException e) {
                LOGGER.warn(Constants.LogMessage.Warn.M2 + e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, Constants.ErrorMessage.E3);
                return;
            }

            session.setAttribute(Constants.SetAttributes.EXPENSES, expenses);
            request.setAttribute(Constants.SetAttributes.EXPENSES, expenses);
            request.setAttribute(Constants.SetAttributes.EMAIL, email);
            request.setAttribute(Constants.SetAttributes.USERNAME, username);

            LOGGER.info(Constants.LogMessage.Info.M1);

            request.getRequestDispatcher(Constants.JspPages.DASHBOARD).forward(request, response);
        } catch (DatabaseConnectionException e) {
            LOGGER.error(Constants.Error.E1 + e.getMessage());
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E1);
            request.getRequestDispatcher(Constants.JspPages.ERROR).forward(request, response);
        } catch (Exception e) {
            LOGGER.error(Constants.Error.E2 + e.getMessage(), e);
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE,  Constants.ErrorMessage.E2);
            request.getRequestDispatcher(Constants.JspPages.ERROR).forward(request, response);
        }
    }
    
    private List<Expense> applyMultipleFilters(List<Expense> expenses, String categoryFilter, 
                                                String paymentModeFilter, int userID, ExpenseDAO expenseDAO) 
                                                throws InvalidFilterException {
        try {
            if (Integer.parseInt(categoryFilter) == 1 && Integer.parseInt(paymentModeFilter) == 1) {
                return expenseDAO.getAllExpenses(userID); 
            } else if (Integer.parseInt(categoryFilter) == 1) {
                return expenseDAO.getExpensesByPaymentMode(userID, Integer.parseInt(paymentModeFilter)); 
            } else if (Integer.parseInt(paymentModeFilter) == 1) {
                return expenseDAO.getExpensesByCategory(userID, Integer.parseInt(categoryFilter));
            } else {
                return expenseDAO.getExpensesByCategoryAndPaymentMode(
                    userID, 
                    Integer.parseInt(categoryFilter), 
                    Integer.parseInt(paymentModeFilter)
                );
            }
        } catch (NumberFormatException e) {
            throw new InvalidFilterException(Constants.ExceptionMessage.InvalidFilterException.M1);
        }
    }

    private List<Expense> applyCategoryFilter(List<Expense> expenses, String categoryFilter, int userID, ExpenseDAO expenseDAO) 
        throws InvalidFilterException {
        try {
            if (Integer.parseInt(categoryFilter) == 1) {
                return expenseDAO.getAllExpenses(userID); 
            } else {
                return expenseDAO.getExpensesByCategory(userID, Integer.parseInt(categoryFilter)); 
            }
        } catch (NumberFormatException e) {
            throw new InvalidFilterException(Constants.ExceptionMessage.InvalidFilterException.M2);
        }
    }

    private List<Expense> applyPaymentModeFilter(List<Expense> expenses, String paymentModeFilter, int userID, ExpenseDAO expenseDAO) 
        throws InvalidFilterException {
        try {
            if (Integer.parseInt(paymentModeFilter) == 1) {
                return expenseDAO.getAllExpenses(userID); 
            } else {
                return expenseDAO.getExpensesByPaymentMode(userID, Integer.parseInt(paymentModeFilter));
            }
        } catch (NumberFormatException e) {
            throw new InvalidFilterException(Constants.ExceptionMessage.InvalidFilterException.M3);
        }
    }
}
