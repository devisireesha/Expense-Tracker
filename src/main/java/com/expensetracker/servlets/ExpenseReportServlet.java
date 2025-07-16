package com.expensetracker.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.dao.UserDAO;
import com.expensetracker.model.Expense;
import com.expensetracker.util.Constants;
import com.expensetracker.exceptions.InvalidReportParameterException;

@WebServlet(Constants.Servlets.EXPENSEREPORTSERVLET)
public class ExpenseReportServlet extends HttpServlet {
    
    private static final Logger LOGGER = LogManager.getLogger(ExpenseReportServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//Remove cache
    	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache"); 
        response.setDateHeader("Expires", 0); 
        
    	HttpSession session = request.getSession();
        String email = (String) session.getAttribute(Constants.GetParameters.EMAIL);
        String username = (String) session.getAttribute(Constants.GetParameters.USERNAME);

        if (email == null) {
            LOGGER.warn(Constants.LogMessage.Warn.M1);
            response.sendRedirect(Constants.JspPages.LOGIN);
            return;
        }

        UserDAO userDAO = new UserDAO();
        int userID = userDAO.getUserIDByEmail(email);

        String reportType = request.getParameter(Constants.GetParameters.REPORTTYPE);
        String startDate = request.getParameter(Constants.GetParameters.STARTDATE);
        String endDate = request.getParameter(Constants.GetParameters.ENDDATE);
        int categoryId = 0;

        try {
            if (request.getParameter(Constants.GetParameters.CATEGORY) != null && !request.getParameter(Constants.GetParameters.CATEGORY).isEmpty()) {
                categoryId = Integer.parseInt(request.getParameter(Constants.GetParameters.CATEGORY));
            }

            if (reportType == null || reportType.isEmpty()) {
                throw new InvalidReportParameterException(Constants.ExceptionMessage.InvalidReportParameterException.M1);
            }

            if (Constants.GetParameters.CATEGORY.equals(reportType) && categoryId <= 0) {
                throw new InvalidReportParameterException(Constants.ExceptionMessage.InvalidReportParameterException.M2);
            }

            ExpenseDAO expenseDAO = new ExpenseDAO();
            Map<String, Object> resultMap = new HashMap<>();

            if (Constants.GetParameters.CATEGORY.equals(reportType)) {
                resultMap = expenseDAO.getCategoryWiseReport(userID, categoryId);
            } else if (Constants.GetParameters.DATERANGE.equals(reportType)) {
                if (startDate == null || endDate == null || startDate.isEmpty() || endDate.isEmpty()) {
                    throw new InvalidReportParameterException(Constants.ExceptionMessage.InvalidReportParameterException.M3);
                }
                resultMap = expenseDAO.getDateRangeReport(userID, startDate, endDate);
            } else {
                throw new InvalidReportParameterException(Constants.ExceptionMessage.InvalidReportParameterException.M4);
            }

            request.setAttribute(Constants.SetAttributes.EXPENSELIST, resultMap.get(Constants.SetAttributes.EXPENSELIST));
            session.setAttribute(Constants.SetAttributes.EXPENSELIST, resultMap.get(Constants.SetAttributes.EXPENSELIST));
            request.setAttribute(Constants.SetAttributes.TOTAL, resultMap.get(Constants.SetAttributes.TOTAL));
            request.setAttribute(Constants.SetAttributes.AVERAGE, resultMap.get(Constants.SetAttributes.AVERAGE));
            request.setAttribute(Constants.SetAttributes.HIGHEST, resultMap.get(Constants.SetAttributes.HIGHEST));
            request.setAttribute(Constants.SetAttributes.LOWEST, resultMap.get(Constants.SetAttributes.LOWEST));
            request.setAttribute(Constants.SetAttributes.REPORTTYPE, reportType);
            request.setAttribute(Constants.SetAttributes.STARTDATE, startDate);
            request.setAttribute(Constants.SetAttributes.ENDDATE, endDate);

            RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.JspPages.EXPENSE_REPORT_RESULT);
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            LOGGER.error(Constants.Error.E5, e);
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE,Constants.ErrorMessage.E27);
            request.getRequestDispatcher(Constants.JspPages.ERROR).forward(request, response);
        } catch (InvalidReportParameterException e) {
            LOGGER.warn(Constants.Error.E4 + e.getMessage(), e);
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE, e.getMessage());
            request.getRequestDispatcher(Constants.JspPages.ERROR).forward(request, response);
        } catch (Exception e) {
            LOGGER.error(Constants.Error.E3 + e.getMessage(), e);
            request.setAttribute(Constants.SetAttributes.ERROR_MESSAGE,Constants.ErrorMessage.E28);
            request.getRequestDispatcher(Constants.JspPages.ERROR).forward(request, response);
        }
    }
}