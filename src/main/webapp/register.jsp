<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Expense Tracker</title>
    <style>
        <%@include file="/WEB-INF/css/register.css"%>
        </style>
    <script>
    function validateRegisterForm() {
        var username = document.getElementById("username").value;
        var email = document.getElementById("email").value;
        var password = document.getElementById("password").value;
        var errorMsg = "";
        
        var usernamePattern = /^[a-zA-Z]+$/;
        if (username === "") {
            errorMsg += "Username is required.\n";
        }else if (!username.match(usernamePattern)) {
            errorMsg += "Invalid username. Only letters are allowed. Please remove any special characters or numbers\n";
        }

        var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        if (email === "") {
            errorMsg += "Email is required.\n";
        } else if (!email.match(emailPattern)) {
            errorMsg += "Please enter a valid email address.\n";
        }

        if (password === "") {
            errorMsg += "Password is required.\n";
        }

        var passwordPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{8,}$/;
        if (password === "") {
            errorMsg += "Password cannot be empty.\n";
        } else if (!password.match(passwordPattern)) {
            errorMsg += "Invalid password! Password should contain at least 1 uppercase, 1 lowercase, 1 digit, 1 special character and be at least 8 characters long.\n";
        }

        if (errorMsg !== "") {
            alert(errorMsg);  
            return false;  
        }

        return true;
    }

   
<%--     <% String errorMessage = (String) request.getAttribute("errorMessage");
       if(errorMessage != null){ %>
        window.onload = function() {
            alert("<%= errorMessage %>");
        };
    <% } %> --%>
    
    
    <% String successMessage = (String) request.getAttribute("successMessage");
       if(successMessage != null) { %>
        window.onload = function() {
            var wantToLogin = confirm("<%= successMessage %> Do you want to log in?");
            if(wantToLogin){
                window.location.href = "login.jsp"; 
            }
        };
    <% } %>
</script>
</head>
<body>
<div class="center-title">
            ExpenseWise
        </div>

    <div class="register-box">
        <div class="register-header">
			<h2>Register</h2>
		</div>
	            <c:if test="${not empty errorMessage}">
	<div class="error-message">
      ${errorMessage != null ? errorMessage : ""}
    </div>
	</c:if>
        <form action="register" method="post" onsubmit="return validateRegisterForm()">
            <div class="input-box">
                <label for="username"></label>
                <input class="input-field" type="text" id="username" name="username" placeholder="Username"
					autocomplete="off" required>
            </div>

            <div class="input-box">
                <label for="password"></label>
                <input class="input-field" type="password" id="password" name="password" placeholder="Password"
					autocomplete="off" required>
            </div>
            
            <div class="input-box">
                <label for="email"></label>
                <input class="input-field" type="email" id="email" name="email" placeholder="Email"
					autocomplete="off" required>
            </div>
            
			<div class="input-box">
                <label for="phone"></label>
                <input class="input-field" type="tel" id="phone" name="phone" placeholder="Phone"
					autocomplete="off" required>
            </div>

			<div class="input-submit">
				<button class="submit-btn" id="submit"></button>
				<label for="submit">Register</label>
			</div>

            <div class="sign-up-link">
                <p>Already have an account? <a href="login.jsp">Login here</a></p>
            </div>   
        </form>
    </div>
</body>
</html>
