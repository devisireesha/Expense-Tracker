package com.expensetracker.model;

import java.sql.Timestamp;

public class UserCategory {
	private int userCategoryID;
    private int userID;
    private int categoryID;
    private boolean isDefault;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
	@Override
	public String toString() {
		return "UserCategory [userCategoryID=" + userCategoryID + ", userID=" + userID + ", categoryID=" + categoryID
				+ ", isDefault=" + isDefault + ", isActive=" + isActive + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
	public int getUserCategoryID() {
		return userCategoryID;
	}
	public int getUserID() {
		return userID;
	}
	public int getCategoryID() {
		return categoryID;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public boolean isActive() {
		return isActive;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUserCategoryID(int userCategoryID) {
		this.userCategoryID = userCategoryID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
    
}

