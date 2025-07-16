<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Add Expense - Expense Tracker</title>
<script>
        function validateExpenseForm() {
            var expenseName = document.getElementById("expenseName").value;
            var categoryID = document.getElementById("categoryID").value;
            var amount = document.getElementById("amount").value;
            var date = document.getElementById("date").value;
            var errorMsg = "";

            if (expenseName === "") {
                errorMsg += "Expense name is required.\n";
            }
            if (categoryID === "") {
                errorMsg += "Category is required.\n";
            }
            if (amount === "" || parseFloat(amount) <= 0) {
                errorMsg += "Amount must be a positive number.\n";
            }
            if (date === "") {
                errorMsg += "Date is required.\n";
            }
            if (errorMsg !== "") {
                alert(errorMsg);
                return false;
            }
            return true;
        }
    </script>
<style>
    <%@ include file="/WEB-INF/css/addExpense.css"%>
</style>
</head>
<body>
	<jsp:include page="navigation.jsp" />
	<div class="container">
		<h2>Add Expense</h2>
	<c:if test="${not empty errorMessage}">
	<div class="error-message">
      ${errorMessage != null ? errorMessage : ""}
    </div>
	</c:if>
		<form action="expense" method="post"
			onsubmit="return validateExpenseForm()">
			<input type="hidden" name="action" value="add">

			<div class="form-group">
				<label for="expenseName">Expense Name:</label> <input type="text"
					id="expenseName" name="expenseName" required>
			</div>

			<div class="form-group">
				<label for="amount">Amount:</label> <input type="number" step="1"
					id="amount" name="amount" required>
			</div>

			<div class="form-group">
				<label for="payee">Payee:</label> <input type="text" id="payee"
					name="payee">
			</div>

			<div class="form-group">
				<label for="type">Type:</label> <select name="type" required>
				<option value="" disabled selected>Select</option>
					<option value="income">Income</option>
					<option value="spent">Spent</option>
				</select>
			</div>

			<div class="form-group">
				<label for="categoryID">Category:</label> <select id="categoryID"
					name="categoryID" required>
					<option value="" disabled selected>Select</option>
					<c:forEach var="category" items="${sessionScope.categories}">
						<option value="${category.categoryID}">${category.categoryName}</option>
					</c:forEach>
				</select>
			</div>

			<div class="form-group">
				<label for="paymentmodes">Payment Mode:</label> <select
					id="paymentmodes" name="paymentID" required>
					<option value="" disabled selected>Select</option>
					<c:forEach var="payment" items="${sessionScope.paymentModes}">
						<option value="${payment.paymentModeID}">${payment.paymentModeName}</option>
					</c:forEach>
				</select>
			</div>

			<div class="form-group">
				<label for="referenceID">Reference ID:</label> <input type="text"
					id="referenceID" name="referenceID">
			</div>

			<div class="form-group">
				<label for="date">Date:</label> <input type="date" id="date"
					name="date" required>
			</div>

			<div class="form-group">
				<label for="description">Description:</label>
				<textarea id="description" name="description"></textarea>
			</div>

			<div style="display:flex; flex-direction: row">
			<button style="width:300px; margin:10px;" type="submit">Add Expense</button>
			<button style="width:300px; margin:10px;" onclick="window.location.href='dashboard.jsp'">Cancel</button>
			</div>
		</form>
	</div>
</body>
</html>

