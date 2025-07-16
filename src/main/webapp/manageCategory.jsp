<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Manage Categories</title>
<style>
<%@ include file="/WEB-INF/css/manageCategory.css"%>
</style>
</head>
<body>
<jsp:include page="navigation.jsp" />
	<div class="container">
		<div class="expenses-container">
			<div class="center-heading">
				<h3 class="expenses-title">Manage Categories</h3>
			</div>
			<div class="rightbutton">
				<div class="addExpense">
					<button onclick="window.location.href='addCategory.jsp'">Add
						New Category</button>
				</div>
			</div>
		</div>
		<table>
			<thead>
				<tr>
					<th style="text-align:center;">Category</th>
					<th style="display:flex;flex-direction: row;justify-content:center;">Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="category" items="${sessionScope.categoriesToManage}">
					<tr>
						<td style="text-align:center;">${category.categoryName}</td>
						<td style="display:flex; flex-direction: row; justify-content:center;">
						
<%-- 					<a href="editCategory.jsp?id=${category.categoryID}">Edit</a> 
					    <a href="deleteCategory?id=${category.categoryID}" onclick="return confirm('Are you sure?');">Delete</a> --%>
					
			    <button type="button" onclick="window.location.href='category?action=edit&categoryID=${category.categoryID}'" style="background-color: #007bff; color: #fff; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer; margin:10px;">
                    Edit
                </button>
					<form action="category" method="post" style="display: inline;" onsubmit="return confirm('Are you sure you want to delete this category?');">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="categoryID" value="${category.categoryID}">
                    <button type="submit" style="background-color: #dc3545; color: #fff; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer;margin: 10px">
                        Delete
                    </button>
                </form>
					</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>