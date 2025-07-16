<%@ page session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Category</title>
    <style>
    <%@ include file="/WEB-INF/css/editCategory.css"%>
    </style>
</head>
<body>
<jsp:include page="navigation.jsp" />
    <div class="container">
    <h2>Edit Category</h2>
    <form action="category" method="POST">
        <input type="hidden" name="categoryID" value="${category.categoryID}" />
        <label for="categoryName">Category Name:</label>
        <input type="text" name="categoryName" value="${category.categoryName}" required>
        <div style="display: flex;justify-content:space-evenly;">
			<button style="margin:10px;" type="submit" onclick="window.location.href='successEdit.jsp'">Save Category</button>
			<button style="margin:10px;" onclick="window.location.href='manageCategory.jsp'">Cancel</button>
		</div>
		<button  onclick="window.location.href='dashboard.jsp'">Go To Dashboard</button>
    </form>
    
    </div>
</body>
</html>

