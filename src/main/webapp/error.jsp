<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error Page</title>
    <style>
        <%@include file="/WEB-INF/css/error.css"%>
    </style>
</head>
<body>
    <jsp:include page="navigation.jsp" />
    <div class="error-container">
        <h1>Oops! Something went wrong.</h1>
        <p>
            <strong>Error Details:</strong> 
            <c:choose>
                <c:when test="${not empty errorMessage}">
                    ${errorMessage}
                </c:when>
                <c:otherwise>
                    An unexpected error occurred. Please try again later.
                </c:otherwise>
            </c:choose>
        </p>
        <c:if test="${not empty exception}">
            <p><strong>Exception:</strong> ${exception.message}</p>
        </c:if>
        <div class="button">
            <a href="dashboard.jsp">Return to Dashboard</a>
        </div>
    </div>
</body>
</html>