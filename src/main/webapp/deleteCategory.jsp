<html>
<head>
<style>
<%@ include file="/WEB-INF/css/deleteExpense.css"%>
</style>
<title>Delete Category</title>
</head>
<body>
	<jsp:include page="navigation.jsp" />
	<div class="container">
		<form action="CategoryServlet" method="POST">
			<input type="hidden" name="categoryID" value="${category.categoryID}">
			<h2>Category deleted successfully.</h2>
		</form>
	</div>
	<div class="container">
		<div class="button-group">
			<button style="color: white; background-color: black;"
				onclick="window.location.href='manageCategory.jsp'">Back to
				Category List</button>
		</div>
	</div>
</body>
</html>

<%-- <div class="error-message">
      ${errorMessage != null ? errorMessage : ""}
    </div> --%>

