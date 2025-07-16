<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
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
<%@include file="/WEB-INF/css/editExpense.css"%>
    </style>
</head>
    <body>
    		<jsp:include page="navigation.jsp" />
        <div class="container">
        <h2>Edit Expense</h2>
            
        <form action="expense" method="POST" onsubmit="return validateExpenseForm()">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="expenseId" value="${expense.expenseID}">

            <label for="expenseName">Expense Name</label>
            <input type="text" id="expenseName" name="expenseName" value="${expense.expenseName}" required>

            <label for="amount">Amount</label>
            <input type="number" id="amount" step="0.01" name="amount" value="${expense.amount}" required>

            <label for="payee">Payee</label>
            <input type="text" name="payee" id="payee" value="${expense.payee}" required>
            


    <label for="type">Type:</label>
    <select name="type" id="type" required>
        <option value="income" ${expense.type == 'income' ? 'selected' : ''}>Income</option>
        <option value="spent" ${expense.type == 'spent' ? 'selected' : ''}>Spent</option>
    </select>

	       
            
            <label for="categoryID">Category</label>
            <select id="categoryID" name="categoryID" required>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.categoryID}" ${category.categoryID == expense.categoryID ? 'selected' : ''}>
                        ${category.categoryName}
                    </option>
                </c:forEach>
            </select>

            <label for="paymentID">Payment Mode</label>
            <select id="paymentID" name="paymentID" required>
                <c:forEach var="paymentMode" items="${paymentModes}">
                    <option value="${paymentMode.paymentModeID}" ${paymentMode.paymentModeID == expense.paymentModeID ? 'selected' : ''}>
                        ${paymentMode.paymentModeName}
                    </option>
                </c:forEach>
            </select>

            <label for="description">Description</label>
            <textarea id="description" name="description" rows="4">${expense.description}</textarea>

            <label for="date">Date</label>
            <input id="date" type="date" name="date" value="${expense.date}" required>


<div  style="display: flex;justify-content:space-evenly;">
			<button style="margin:10px;" type="submit" onclick="window.location.href='successEdit.jsp'">Save Expense</button>
			<button style="margin:10px;" type="button" onclick="window.location.href='dashboard.jsp'">Cancel</button>
</div>
        </form>
        </div>
    </body>
</html>