<%@ page session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>View Expense Details</title>
<style>
<%@ include file="/WEB-INF/css/viewExpense.css"%>
</style>
</head>
<body>
    <jsp:include page="navigation.jsp" />

    <div class="container">
        <h2>Expense Details</h2>
        <form action="expense" method="get" onsubmit="return validateExpenseForm()">
            <input type="hidden" name="action" value="view">
            <input type="hidden" name="expenseId" value="${expense.expenseID}">
            <table>
                <tr>
                    <th>Expense Name:</th>
                    <td>${expense.expenseName}</td>
                </tr>
                <tr>
                    <th>Amount:</th>
                    <td>${expense.amount}</td>
                </tr>
                <tr>
                    <th>Type:</th>
                    <td>${expense.type}</td>
                </tr>
                <tr>
                    <th>Date:</th>
                    <td>${expense.date}</td>
                </tr>
                <tr>
                    <th>Payee:</th>
                    <td>${expense.payee}</td>
                </tr>
                <tr>
                    <th>Category:</th>
                    <td>${expense.categoryName}</td>
                </tr>
                <tr>
                    <th>Payment Mode:</th>
                    <td>${expense.paymentModeName}</td>
                </tr>
                <tr>
                    <th>Description:</th>
                    <td>${expense.description}</td>
                </tr>
            </table>
        </form>
        <div style="display: flex; flex-direction: row; justify-content: center; padding: 10px;">
                <button type="button" onclick="window.location.href='dashboard.jsp'" style="background-color: #000; color: #fff; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer; margin: 10px; width: auto; text-align: center;">
                    Back to Dashboard
                </button>
                <button type="button" onclick="window.location.href='expense?action=edit&expenseID=${expense.expenseID}'" style="background-color: #007bff; color: #fff; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer; margin: 10px; width: auto; text-align: center;">
                    Edit
                </button>
                
                <form action="expense" method="post" style="display: inline;" onsubmit="return confirm('Are you sure you want to delete this expense?');">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="expenseID" value="${expense.expenseID}">
                    <button type="submit" style="background-color: #dc3545; color: #fff; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer; margin: 10px; width: auto; text-align: center;">
                        Delete
                    </button>
                </form>
            </div>
    </div>
</body>
</html>
