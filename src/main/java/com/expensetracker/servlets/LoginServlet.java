package com.expensetracker.servlets;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import com.expensetracker.dao.UserDAO;
import com.expensetracker.dao.CategoryDAO;
import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.dao.PaymentModeDAO;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.model.PaymentMode;
import com.expensetracker.model.User;
import com.expensetracker.util.Constants;

import javax.servlet.annotation.WebServlet;
import java.util.List;

@WebServlet(Constants.Servlets.LOGIN)
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(LoginServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//Remove cache
    	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache"); 
        response.setDateHeader("Expires", 0); 
        
    	
    	try {
            String email = request.getParameter(Constants.UserTable.EMAIL);
            String password = request.getParameter(Constants.UserTable.PASSWORD);

            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            	request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E8);
                throw new IllegalArgumentException(Constants.ExceptionMessage.IllegalArgumentException.M1);
            }

            UserDAO userDAO = new UserDAO();
            int userID = userDAO.getUserIDByEmail(email);

            if (userID < 0) {
                request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E9);
                RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.LOGIN);
                dispatcher.forward(request, response);
                return;
            }

            User user = userDAO.getUserByID(userID);
            if (user == null) {
                throw new NullPointerException(Constants.ExceptionMessage.NullPointerException.M1);
            }

            boolean isValidUser = userDAO.loginUser(email, password);
            if (isValidUser && userID > 0) {
                HttpSession session = request.getSession();
                session.setAttribute(Constants.SetAttributes.EMAIL, email);
                session.setAttribute(Constants.SetAttributes.USERNAME, user.getUsername());

                CategoryDAO categoryDAO = new CategoryDAO();
                List<Category> categories = categoryDAO.getCategoriesForUser(userID);
                session.setAttribute(Constants.SetAttributes.CATEGORIES, categories);

                PaymentModeDAO paymentModeDAO = new PaymentModeDAO();
                List<PaymentMode> paymentmodes = paymentModeDAO.getAllPaymentModes();
                session.setAttribute(Constants.SetAttributes.PAYMENTMODES, paymentmodes);

//                List<Category> categoriesToManage = categoryDAO.getCategoriesForUserToManage(userID);
//                session.setAttribute("categoriesToManage", categoriesToManage);

                ExpenseDAO expenseDAO = new ExpenseDAO();
                List<Expense> expenses = expenseDAO.getExpensesByUser(userID);

                double[] result = expenseDAO.calculateIncomeSpentRemaining(userID);
                double income = result[0];
                double spent = result[1];
                double remaining = result[2];

                session.setAttribute(Constants.SetAttributes.INCOME, income);
                session.setAttribute(Constants.SetAttributes.SPENT, spent);
                session.setAttribute(Constants.SetAttributes.REMAINING, remaining);

                request.setAttribute(Constants.SetAttributes.INCOME, income);
                request.setAttribute(Constants.SetAttributes.SPENT, spent);
                request.setAttribute(Constants.SetAttributes.REMAINING, remaining);

                session.setAttribute(Constants.SetAttributes.EXPENSES, expenses);
                request.setAttribute(Constants.SetAttributes.EXPENSES, expenses);

                response.sendRedirect(Constants.JspPages.DASHBOARD);
            } else {
                request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E9);
                RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.ERROR);
                dispatcher.forward(request, response);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(Constants.LogMessage.Error.M4 + e.getMessage(), e);
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E11 + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.LOGIN);
            dispatcher.forward(request, response);
        } catch (NullPointerException e) {
            LOGGER.error(Constants.LogMessage.Error.M5 + e.getMessage(), e);
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E10);
            RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.ERROR);
            dispatcher.forward(request, response);
        } catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M6 + e.getMessage(), e);
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E2);
            RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.ERROR);
            dispatcher.forward(request, response);
        }
    }
}