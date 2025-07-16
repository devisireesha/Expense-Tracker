
package com.expensetracker.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Expense {
    private int expenseID; //change to Id
    private String expenseName;
    private int userID;
    private double amount;
    private String payee;
    private String type;
	private int categoryID;
    private String categoryName;
	private String paymentModeName;
	private int paymentModeID;
    private String status;
    private String referenceID;
    private String description;
    private Date date;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
    @Override
	public String toString() {
		return "Expense [expenseID=" + expenseID + ", expenseName=" + expenseName + ", userID=" + userID + ", amount="
				+ amount + ", payee=" + payee + ", type=" + type + ", categoryID=" + categoryID + ", categoryName="
				+ categoryName + ", paymentModeName=" + paymentModeName + ", paymentModeID=" + paymentModeID
				+ ", status=" + status + ", referenceID=" + referenceID + ", description=" + description + ", date="
				+ date + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

	public String getPaymentModeName() {
		return paymentModeName;
	}
    
	public void setPaymentModeName(String paymentModeName) {
		this.paymentModeName = paymentModeName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

    public int getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(int expenseID) {
        this.expenseID = expenseID;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getPaymentModeID() {
        return paymentModeID;
    }

    public void setPaymentModeID(int paymentModeID) {
        this.paymentModeID = paymentModeID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(String referenceID) {
        this.referenceID = referenceID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
