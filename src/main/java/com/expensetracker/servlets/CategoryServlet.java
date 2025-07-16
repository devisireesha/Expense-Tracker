package com.expensetracker.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.expensetracker.dao.CategoryDAO;
import com.expensetracker.dao.PaymentModeDAO;
import com.expensetracker.dao.UserDAO;
import com.expensetracker.model.Category;
import com.expensetracker.model.PaymentMode;
import com.expensetracker.util.Constants;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(Constants.Servlets.CATEGORY)
public class CategoryServlet extends HttpServlet {
	private static final Logger LOGGER = LogManager.getLogger(CategoryServlet.class);
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Remove cache
    	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache"); 
        response.setDateHeader("Expires", 0); 
        
    	String action = request.getParameter(Constants.GetParameters.ACTION);
        if (Constants.Action.ADD.equals(action)) {
            addCategory(request, response);
        } else if (Constants.Action.UPDATE.equals(action)) {
            updateCategory(request, response);
        } else if (Constants.Action.DELETE.equals(action)) {
            deleteCategory(request, response);
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//Remove cache
    	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache"); 
        response.setDateHeader("Expires", 0); 
        
    	String categoryID = request.getParameter(Constants.GetParameters.CATEGORYID);
    	if (categoryID != null && !categoryID.isEmpty()) {
			try {
				int id = Integer.parseInt(categoryID);
				CategoryDAO categoryDAO = new CategoryDAO();
				Category category = categoryDAO.getCategoryByID(id);
				request.setAttribute(Constants.SetAttributes.CATEGORY, category);
				RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.EDIT_CATEGORY);
				dispatcher.forward(request, response);
			}catch (NumberFormatException e) {
				LOGGER.error(Constants.LogMessage.Error.M19, e);
				request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E24);
				request.getRequestDispatcher(Constants.JspPages.ERROR).forward(request, response);
			}
		} else {
			LOGGER.warn(Constants.LogMessage.Warn.M5);
			request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E14);
			request.getRequestDispatcher(Constants.JspPages.DASHBOARD).forward(request, response);
		}
    }
    

    private void addCategory(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String categoryName = request.getParameter(Constants.GetParameters.CATEGORYNAME);
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute(Constants.GetParameters.EMAIL);
        String username = (String) session.getAttribute(Constants.GetParameters.USERNAME);

        if (email == null) {
            response.sendRedirect(Constants.JspPages.LOGIN);
            return;
        }


        UserDAO userDAO = new UserDAO();
        int userID = userDAO.getUserIDByEmail(email);

        Category category = new Category();
        category.setCategoryName(categoryName);

        CategoryDAO categoryDAO = new CategoryDAO();
        boolean isAdded = categoryDAO.addCategory(category,userID);

        if (isAdded) {
            response.sendRedirect(Constants.JspPages.MANAGE_CATEGORY);
        } else {
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E23);
            RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.ERROR);
            dispatcher.forward(request, response);
        }
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int categoryID = Integer.parseInt(request.getParameter(Constants.GetParameters.CATEGORYID));
        String categoryName = request.getParameter(Constants.GetParameters.CATEGORYNAME);

        Category category = new Category();
        category.setCategoryID(categoryID);
        category.setCategoryName(categoryName);

        CategoryDAO categoryDAO = new CategoryDAO();
        boolean isUpdated = categoryDAO.updateCategory(category);

        if (isUpdated) {
            response.sendRedirect(Constants.JspPages.MANAGE_CATEGORY);
        } else {
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E26);
            RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.EDIT_CATEGORY);
            dispatcher.forward(request, response);
        }
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int categoryID = Integer.parseInt(request.getParameter(Constants.GetParameters.CATEGORYID));

        CategoryDAO categoryDAO = new CategoryDAO();
        boolean isDeleted = categoryDAO.deleteCategory(categoryID);

        if (isDeleted) {
            response.sendRedirect(Constants.JspPages.MANAGE_CATEGORY);
        } else {
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, Constants.ErrorMessage.E25);
            RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.DELETE_CATEGORY);
            dispatcher.forward(request, response);
        }
    }
}


