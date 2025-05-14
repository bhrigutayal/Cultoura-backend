package com.tourism.Cultoura.ResponseModels;

import java.math.BigDecimal;

public class PaymentDetailsResponse {
    private Long itineraryId;
    private BigDecimal totalAmount;
    private String paymentMethods; // Example: "UPI, Credit/Debit Card, PayPal"
    
    public PaymentDetailsResponse(Long itineraryId, BigDecimal totalAmount, String paymentMethods) {
		super();
		this.itineraryId = itineraryId;
		this.totalAmount = totalAmount;
		this.paymentMethods = paymentMethods;
	}
    
    
	public Long getItineraryId() {
		return itineraryId;
	}
	public void setItineraryId(Long itineraryId) {
		this.itineraryId = itineraryId;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public String getPaymentMethods() {
		return paymentMethods;
	}
	public void setPaymentMethods(String paymentMethods) {
		this.paymentMethods = paymentMethods;
	}
}