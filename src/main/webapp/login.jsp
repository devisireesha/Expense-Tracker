<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login - Expense Tracker</title>
<style>
<%@include file="/WEB-INF/css/login.css"%>
</style>
</head>
<body>
<div class="center-title">
            ExpenseWise
        </div>
	<div class="login-box">
		<div class="login-header">
			<header>Login</header>
		</div>
	<c:if test="${not empty errorMessage}">
	<div class="error-message">
      ${errorMessage != null ? errorMessage : ""}
    </div>
	</c:if>
		<form action="login" method="post">
			<div class="input-box">
				<label for=email></label> <input class="input-field"
					type="email" id="email" name="email" placeholder="email"
					 required>
			</div>

			<div class="input-box">
				<label for="password"></label> <input class="input-field"
					type="password" id="password" name="password"
					placeholder="Password" required>
			</div>

			<div class="input-submit">
				<button class="submit-btn" id="submit"></button>
				<label for="submit">login</label>
			</div>

			<div class="sign-up-link">
				<p>
					Don't have an account? <a href="register.jsp">Register here</a>
				</p>
			</div>
		</form>
	</div>
</body>
</html>
