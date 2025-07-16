<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
    <%@ include file="/WEB-INF/css/editCategory.css"%>
    </style>
    <title>Add Category - Expense Tracker</title>
        <script>
        function validateCategoryForm() {
            var categoryName = document.getElementById("categoryName").value;
            var errorMsg = "";

            if (categoryName === "") {
                errorMsg += "Category name is required.\n";
            }

            if (errorMsg !== "") {
                alert(errorMsg);
                return false;
            }

            return true;
        }
    </script>
</head>
<body>
<jsp:include page="navigation.jsp" />
    <div class="container">
        <h2>Add Category</h2>

        <form action="category" method="post" onsubmit="return validateCategoryForm()">
            <input type="hidden" name="action" value="add">
 <%--            <input type="hidden" name="userid" value="${sessionScope.userid}"> --%>
            

                <label for="categoryName">Category Name:</label>
                <input type="text" id="categoryName" name="categoryName" required>

            <div class="form-group">
            <div style="display: flex;justify-content:space-evenly;">
			<button style="margin: 10px;" type="submit">Add Expense</button>
			<button style="margin: 10px;" onclick="window.location.href='dashboard.jsp'">Cancel</button>
</div>
            </div>
        </form>
    </div>
</body>
</html>
