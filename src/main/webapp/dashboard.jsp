<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Dashboard - Expense Tracker</title>
<style>
<%@ include file="/WEB-INF/css/dashboard.css"%>
</style>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
function toggleStatistics() {
    var container = document.getElementById("statistics-container");
    container.style.display = container.style.display === "none" ? "block" : "none";

    if (container.style.display === "block") {
        renderIncomeSpentChart();
    }
}

function renderIncomeSpentChart() {
    var income = parseFloat("${sessionScope.income}");
    var spent = parseFloat("${sessionScope.spent}");
 	var ctx = document.getElementById('incomeSpentChart').getContext('2d');
    var incomeSpentChart = new Chart(ctx, {
        type: 'bar', 
        data: {
            labels: ['Income', 'Spent'], 
            datasets: [{
                label: 'Income', 
                data: [income, 0],
                backgroundColor: '#28a745',
                borderColor: '#28a745',
                borderWidth: 1
            }, {
                label: 'Spent',
                data: [0, spent],
                backgroundColor: '#dc3545',
                borderColor: '#dc3545',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            },
            plugins: {
                legend: {
                    position: 'top'
                }
            }
        }
    });
}
</script>
</head>
<body>
	<jsp:include page="navigation.jsp" />
	<div class="container">
		<h2>Hello, ${sessionScope.username}</h2>
	 <c:if test="${not empty errorMessage}">
	<div class="error-message">
      ${errorMessage != null ? errorMessage : ""}
    </div>
	</c:if>
		<div class="filter-options">
			<form action="dashboard" method="get">
				<label for="categories">Filter by Category:</label> <select
					name="categoryFilter" id="categories">
					<option value="" disabled selected>Select Category</option>
					<c:forEach var="category" items="${sessionScope.categories}">
						<option value="${category.categoryID}">${category.categoryName}</option>
					</c:forEach>
				</select> <label for="paymentmodes">Filter by Payment Mode:</label> <select
					id="paymentmodes" name="paymentModeFilter">
					<option value="" disabled selected>Select Payment Mode</option>
					<c:forEach var="payment" items="${sessionScope.paymentModes}">
						<option value="${payment.paymentModeID}">${payment.paymentModeName}</option>
					</c:forEach>
				</select>
				<button type="submit"
					style="background-color: #000; color: #fff; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer;">Apply
					Filter</button>
			</form>
		</div>

		<div class="dashboard-summary">
			<div class="cell">
				<h2>Income</h2>
				<p>${sessionScope.income}</p>
			</div>
			<div class="cell">
				<h2>Spent</h2>
				<p>${sessionScope.spent}</p>
			</div>
			<div class="cell">
				<h2>Remaining</h2>
				<p>${sessionScope.remaining}</p>
			</div>
		</div>

		<div class="expenses-container">
			<div class="center-heading">
				<h3 class="expenses-title">Expenses</h3>
			</div>
			<div class="rightbutton">
				<div class="addExpense">
					<button onclick="window.location.href='addExpense.jsp'">Add
						New Expense</button>
				</div>
			</div>
		</div>
		<c:if test="${not empty expenses}">
			<table>
				<thead>
					<tr>
						<th>Expense Name</th>
						<th>Amount</th>
						<th>Type</th>
						<th>Date</th>
						<th>Payee</th>
						<th>Category</th>
						<th>Payment Mode</th>
						<th>Description</th>
						<th style="width: 120px;">Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="expense" items="${sessionScope.expenses}">
						<tr>
							<td>${expense.expenseName}</td>
							<td>${expense.amount}</td>
							<td>${expense.type}</td>
							<td>${expense.date}</td>
							<td>${expense.payee}</td>
							<td>${expense.categoryName}</td>
							<td>${expense.paymentModeName}</td>
							<!-- Display Payment Mode -->
							<td>${expense.description}</td>
							<td
								style="text-align: center; display: flex; justify-content: center; align-items: center;">

								<%-- <button type="button"
        onclick="window.location.href='expense?action=edit&expenseID=${expense.expenseID}'"
        style="background-color: #007bff; color: #fff; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer; margin: 2px; width: auto; text-align: center;">
    Edit
</button>
 --%>
								<button type="button"
									style="background-color: #6c757d; color: #fff; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer;"
									onclick="window.location.href='expense?action=view&expenseID=${expense.expenseID}'">
									View Details</button>

								<form action="expense" method="post" style="display: inline;"
									onsubmit="return confirm('Are you sure you want to delete this expense?');">
									<input type="hidden" name="action" value="delete"> <input
										type="hidden" name="expenseID" value="${expense.expenseID}">
									<button type="submit"
										style="background-color: #dc3545; color: #fff; border: none; padding: 7px 12px; border-radius: 4px; cursor: pointer; margin: 2px; width: auto; text-align: center;">
										<svg viewBox="0 0 24 24" fill="none"
											xmlns="http://www.w3.org/2000/svg" stroke="#fff" width="16"
											height="15">
            <path d="M10 12V17" stroke="fff" stroke-width="2"
												stroke-linecap="round" stroke-linejoin="round"></path>
            <path d="M14 12V17" stroke="fff" stroke-width="2"
												stroke-linecap="round" stroke-linejoin="round"></path>
            <path d="M4 7H20" stroke="fff" stroke-width="2"
												stroke-linecap="round" stroke-linejoin="round"></path>
            <path
												d="M6 10V18C6 19.6569 7.34315 21 9 21H15C16.6569 21 18 19.6569 18 18V10"
												stroke="fff" stroke-width="2" stroke-linecap="round"
												stroke-linejoin="round"></path>
            <path
												d="M9 5C9 3.89543 9.89543 3 11 3H13C14.1046 3 15 3.89543 15 5V7H9V5Z"
												stroke="fff" stroke-width="2" stroke-linecap="round"
												stroke-linejoin="round"></path>
        </svg>
									</button>
								</form>
							</td>

						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
		<c:if test="${empty expenses}">
			<p>No expenses available.</p>
		</c:if>


<!-- 		<div class="category-management">
			<button onclick="window.location.href='manageCategory.jsp'">Manage
				Categories</button>
		</div> -->


		<div class="reports">
			<button onclick="window.location.href='expenseReportForm.jsp'">Generate
				Expense Report</button>
			<button onclick="toggleStatistics()"
				style="background-color: #007bff; color: #fff;">Show
				Statistics</button>
			<div id="statistics-container" style="display: none;">
				<h3>Income vs Spent</h3>
				<canvas id="incomeSpentChart"></canvas>
			</div>
		</div>

	</div>
</body>
</html>

