package com.expensetracker.exceptions;

public class InvalidReportParameterException extends Exception {
 public InvalidReportParameterException(String message) {
     super(message);
 }

 public InvalidReportParameterException(String message, Throwable cause) {
     super(message, cause);
 }
}