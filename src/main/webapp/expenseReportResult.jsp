<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Expense Report</title>
    <style>
 * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}


body {
    font-family: Arial, sans-serif;
    font-size: 16px;
    line-height: 1.5;
    color: #333;
    background-color: #f9f9f9;
}


h2, h3 {
    text-align: center;
    color: #2c3e50;
    margin-bottom: 20px;
}

h2 {
    font-size: 26px;
    text-transform: uppercase;
    letter-spacing: 1px;
}

h3 {
    font-size: 22px;
    margin-top: 20px;
}


ul {
    margin: 20px auto;
    padding: 10px;
    max-width: 600px;
    background: #ffffff;
    border: 1px solid #ddd;
    border-radius: 5px;
    list-style-type: none;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

ul li {
    padding: 8px 10px;
    border-bottom: 1px solid #ddd;
}

ul li:last-child {
    border-bottom: none;
}

ul li span {
    font-weight: bold;
}


table {
    width: 70%;
    border-collapse: collapse;
    margin: 20px auto;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    overflow-x: auto;
    background-color: #ffffff;
}

table thead {
    background-color: #000000;
    color: white;
}

table th, table td {
    text-align: left;
    padding: 12px 15px;
    border: 1px solid #ddd;
}

table tbody tr:nth-child(even) {
    background-color: #f9f9f9;
}

table tbody tr:hover {
    background-color: #f1f1f1;
    cursor: pointer;
}

table th {
    font-weight: bold;
    text-transform: capitalize;
}

table td {
    color: #555;
    text-align: center;
}


p.no-results {
    color: #e74c3c;
    font-size: 16px;
    text-align: center;
    margin: 20px 0;
    font-style: italic;
}


@media (max-width: 600px) {
    body {
        font-size: 14px;
        padding: 10px;
    }

    table th, table td {
        padding: 8px 10px;
    }

    ul li {
        padding: 6px 8px;
    }

    h2, h3 {
        font-size: 18px;
    }
}
    </style>
</head>
<body>
		<jsp:include page="navigation.jsp" />
    <h2>Expense Report</h2>

    <c:choose>
    <c:when test="${reportType == 'dateRange'}">
        <h3>Statistics:</h3>
        <table border="1">
            <thead>
                <tr>
                    <th>Metric</th>
                    <th>Value</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Total Expense</td>
                    <td>Rs. ${total}</td>
                </tr>
                <tr>
                    <td>Average Expense</td>
                    <td>Rs. ${average}</td>
                </tr>
                <tr>
                    <td>Highest Expense</td>
                    <td>Rs. ${highest}</td>
                </tr>
                <tr>
                    <td>Lowest Expense</td>
                    <td>Rs. ${lowest}</td>
                </tr>
            </tbody>
        </table>
    </c:when>
</c:choose>

    <h3>Expense Details:</h3>

    <c:choose>
        <c:when test="${reportType == 'category'}">
            <c:if test="${not empty expenseList}">
                <table border="1">
                    <thead>
                        <tr>
                            <th>Category</th>
                            <th>Total Amount</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="expense" items="${expenseList}">
                            <tr>
                                <td>${expense.categoryName}</td>
                                <td>Rs. ${expense.amount}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${empty expenseList}">
                <p class="no-results">No expenses found for this category.</p>
            </c:if>
        </c:when>

        <c:when test="${reportType == 'dateRange'}">
            <c:if test="${not empty expenseList}">
                <table border="1">
                    <thead>
                        <tr>
                        	<th>Expense Name</th>
                            <th>Category</th>
                            <th>Amount</th>
                            <th>Expense Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="expense" items="${expenseList}">
                            <tr>
                            	<td>${expense.expenseName}</td>
                                <td>${expense.categoryName}</td>
                                <td>Rs. ${expense.amount}</td>
                                <td>${expense.date}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${empty expenseList}">
                <p class="no-results">No expenses found in the given date range.</p>
            </c:if>
        </c:when>
    </c:choose>
</body>
</html>