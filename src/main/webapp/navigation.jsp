<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
        }
        .navigation {
            background-color: #000;
            color: white;
            padding: 10px 30px;
            display: flex;
            align-items: center;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
        }
        .button-group {
            display: flex;
            align-items: center;
        }
        .button-group button {
            border: none;
            padding: 10px 15px;
            margin: 0 5px;
            font-size: 14px;
            cursor: pointer;
            background-color: #fff;
            color: #000;
            border-radius: 5px;
            transition: 0.3s;
        }
        .button-group button:hover {
            background-color: white;
            color: black;
        }
        .center-title {
            flex-grow: 1;
            margin-left : 150px;
            text-align: center;
            font-size: 30px;
            font-weight: bold;
        }
        .right-nav {
            display: flex;
            align-items: center;
        }
        .right-nav img {
            width: 35px;
            height: 35px;
            border-radius: 50%;
            margin-right: 10px;
        }
        .right-nav b {
            margin-right: 20px;
        }
    </style>
</head>
<body>
    <div class="navigation">
        <div class="left-nav button-group">
            <button onclick="window.history.back()">Back</button>
        </div>
        <div class="center-title">
            ExpenseWise
        </div>
        <div class="right-nav">
            <img src='<c:url value="/img/download.jpg" />' alt="User Icon" />
            <b>${sessionScope.username != null ? sessionScope.username : 'Guest'}</b>
            <div class="button-group">
                <button onclick="window.location.href='logout'">Logout</button>
            </div>
        </div>
    </div>
</body>
</html>