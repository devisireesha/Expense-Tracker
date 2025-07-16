package com.expensetracker.model;


public class PaymentMode {
    private int paymentModeID;
    private String paymentModeName;

    public PaymentMode() {}

    public PaymentMode(int paymentModeID, String paymentModeName) {
        this.paymentModeID = paymentModeID;
        this.paymentModeName = paymentModeName;
    }

    public int getPaymentModeID() {
        return paymentModeID;
    }

    public void setPaymentModeID(int paymentModeID) {
        this.paymentModeID = paymentModeID;
    }

    @Override
	public String toString() {
		return "PaymentMode [paymentModeID=" + paymentModeID + ", paymentModeName=" + paymentModeName + "]";
	}

	public String getPaymentModeName() {
        return paymentModeName;
    }

    public void setPaymentModeName(String paymentModeName) {
        this.paymentModeName = paymentModeName;
    }
}
