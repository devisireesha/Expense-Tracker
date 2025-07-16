package com.expensetracker.model;

public class Category {
	private int categoryID;
    private String categoryName;
    private boolean isDefault;

    public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    @Override
	public String toString() {
		return "Category [categoryID=" + categoryID + ", categoryName=" + categoryName + "]";
	}
}