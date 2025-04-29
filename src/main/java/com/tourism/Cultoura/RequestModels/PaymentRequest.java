package com.tourism.Cultoura.RequestModels;
import java.math.BigDecimal;

public class PaymentRequest {
    private Long userId;
    private Long itineraryId;
    private BigDecimal totalAmount;
    private String paymentMethod; // e.g., "Credit Card", "UPI", "Net Banking"
    private String currency;
    private String cardNumber;  // Optional: Required only for card payments
    private String expiryDate;  // Optional
    private String cvv;         // Optional
    private String upiId;       // Optional: Required only for UPI payments

    public PaymentRequest() {}

    public PaymentRequest(Long userId, Long itineraryId, BigDecimal totalAmount, String paymentMethod, 
                          String currency, String cardNumber, String expiryDate, String cvv, String upiId) {
        this.userId = userId;
        this.itineraryId = itineraryId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.currency = currency;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.upiId = upiId;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }
}
