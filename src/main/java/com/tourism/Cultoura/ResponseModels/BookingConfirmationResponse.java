package com.tourism.Cultoura.ResponseModels;

public class BookingConfirmationResponse {
    private Long itineraryId;
    private String message;
	public Long getItineraryId() {
		return itineraryId;
	}
	public void setItineraryId(Long itineraryId) {
		this.itineraryId = itineraryId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}