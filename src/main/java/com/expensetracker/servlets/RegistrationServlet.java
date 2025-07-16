package com.expensetracker.servlets;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.expensetracker.dao.UserCategoryDAO;
import com.expensetracker.dao.UserDAO;
import com.expensetracker.model.User;
import com.expensetracker.util.Constants;
import com.expensetracker.util.DBConnection;
import com.expensetracker.exceptions.UserAlreadyExistsException;

import javax.servlet.annotation.WebServlet;

@WebServlet(Constants.Servlets.REGISTER)
public class RegistrationServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(RegistrationServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//Remove cache
    	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache"); 
        response.setDateHeader("Expires", 0); 
        
    	
    	String username = request.getParameter(Constants.GetParameters.USERNAME);
        String password = request.getParameter(Constants.GetParameters.PASSWORD);
        String email = request.getParameter(Constants.GetParameters.EMAIL);
        String phone = request.getParameter(Constants.GetParameters.PHONE);

        UserDAO userDAO = new UserDAO();
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);

        try {
            if (userDAO.isUserExist(email, phone)) {
                throw new UserAlreadyExistsException(Constants.ExceptionMessage.UserAlreadyExistsException.M1);
            }

            try (Connection connection = DBConnection.getConnection()) {
                connection.setAutoCommit(false);

                int userId = userDAO.registerUser(user);
                UserCategoryDAO userCategoryDAO = new UserCategoryDAO();
                if (userId > 0) {
                    userCategoryDAO.assignDefaultCategories(userId);
                    connection.commit();
                    response.sendRedirect(Constants.JspPages.LOGIN);
                } else {
                    connection.rollback();
                    request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E4);
                    RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.REGISTER);
                    dispatcher.forward(request, response);
                }
            } catch (SQLException e) {
                LOGGER.error(Constants.LogMessage.Error.M2+ e.getMessage(), e);
                request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E5);
                RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.ERROR);
                dispatcher.forward(request, response);
            } catch (Exception e) {
                LOGGER.error(Constants.LogMessage.Error.M3 + e.getMessage(), e);
                request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E2);
                RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.ERROR);
                dispatcher.forward(request, response);
            }

        } catch (UserAlreadyExistsException e) {
            LOGGER.warn(Constants.LogMessage.Warn.M3+ e.getMessage(), e);
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E6);
            RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.REGISTER);
            dispatcher.forward(request, response);
        } catch (Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M1+ e.getMessage(), e);
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E7);
            RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.ERROR);
            dispatcher.forward(request, response);
        }
    }
}