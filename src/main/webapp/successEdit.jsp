<html>
<head>
<style>
<%@ include file="/WEB-INF/css/deleteExpense.css"%>
</style>
<title>Update Expense</title>
</head>
<body>
	<jsp:include page="navigation.jsp" />
	<div class="container">
		<form action="ExpenseServlet" method="POST">
			<input type="hidden" name="expenseId" value="${expense.expenseID}">
			<h2>Expense Updated successfully.</h2>
		</form>
	</div>
	<div class="container">
		<div class="button-group">
			<button style="color: white; background-color: black;"
				onclick="window.location.href='dashboard.jsp'">Back to
				Expenses List</button>
		</div>
	</div>
</body>
</html>
