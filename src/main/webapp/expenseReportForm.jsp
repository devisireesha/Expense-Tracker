<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Expense Report Form</title>
    <style>
    * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: Arial, sans-serif;
    font-size: 16px;
    line-height: 1.5;
    color: #333;
    background-color: #f4f4f9;
    top: 200px;
}

h1 {
    color: #2c3e50;
    font-size: 24px;
    margin-bottom: 20px;
    text-align: center;
}

form {
    max-width: 600px;
    margin: 0 auto;
    background: #ffffff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    border: 1px solid #ddd;
}

form label {
    display: block;
    margin-bottom: 10px;
    font-weight: bold;
    color: #555;
}

form select, 
form input[type="date"],
form button {
    display: block;
    width: 100%;
    padding: 10px;
    font-size: 14px;
    border: 1px solid #ddd;
    border-radius: 4px;
    margin-bottom: 15px;
    outline: none;
    background-color: #f9f9f9;
    transition: background-color 0.3s ease;
}

form select:focus, 
form input[type="date"]:focus {
    border-color: #3498db;
    background-color: #eaf6fe;
}

form button {
    background-color: #000000;
    color: white;
    cursor: pointer;
    font-weight: bold;
    transition: background-color 0.3s ease, transform 0.2s ease;
}

form button:hover {
    background-color: #333;
    transform: translateY(-1px);
}

p[style*="color: red;"] {
    font-size: 14px;
    color: #e74c3c;
    margin-bottom: 15px;
}

#categoryFields,
#dateRangeFields {
    display: none;
    padding: 10px 0;
}

.text-center {
    text-align: center;
}

    </style>
</head>
<body>
    <jsp:include page="navigation.jsp" />
    <h1>Generate Expense Report</h1>

 
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>

    <form action="ExpenseReportServlet" method="post">
        <label for="reportType">Select Report Type:</label>
        <select name="reportType" id="reportType" required>
            <option value="" disabled selected>-- Select Report Type --</option>
            <option value="category">Category Wise Report</option>
            <option value="dateRange">Date Range Report</option>
        </select>

        <div id="categoryFields" style="display: none;">
            <h1>Choose a category for the report:</h1>
            <label for="category">Category:</label>
            <select name="category" id="category">

                <c:forEach var="category" items="${categories}">
                    <option value="${category.categoryID}">${category.categoryName}</option>
                </c:forEach>
            </select>
        </div>


        <div id="dateRangeFields" style="display: none;">
            <h1>Choose the date range:</h1>
            <label for="startDate">Start Date:</label>
            <input type="date" name="startDate" id="startDate">
            <br><br>
            <label for="endDate">End Date:</label>
            <input type="date" name="endDate" id="endDate">
        </div>

        <button type="submit">Generate Report</button>
    </form>


    <script type="text/javascript">
        const reportTypeSelect = document.getElementById('reportType');
        const categoryFields = document.getElementById('categoryFields');
        const dateRangeFields = document.getElementById('dateRangeFields');
        const categoryField = document.getElementById('category');
        const startDateField = document.getElementById('startDate');
        const endDateField = document.getElementById('endDate');

        reportTypeSelect.addEventListener('change', function() {
            
            categoryField.required = false;
            startDateField.required = false;
            endDateField.required = false;

            if (this.value === 'category') {
               
                categoryFields.style.display = 'block';
                dateRangeFields.style.display = 'none';
                categoryField.required = true;
            } else if (this.value === 'dateRange') {
               
                categoryFields.style.display = 'none';
                dateRangeFields.style.display = 'block';
                startDateField.required = true;
                endDateField.required = true;
            } else {
                
                categoryFields.style.display = 'none';
                dateRangeFields.style.display = 'none';
            }
        });
    </script>
</body>
</html>