package com.tourism.Cultoura.RequestModels;

public class BookingRequest {
    // For this demo we include a simple field.
    // In a real scenario, you might pass additional booking details.
    private String userName;

    public BookingRequest() {}

    // Getter and Setter
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}