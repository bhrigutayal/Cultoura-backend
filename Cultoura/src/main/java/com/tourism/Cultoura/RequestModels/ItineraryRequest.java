package com.tourism.Cultoura.RequestModels;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ItineraryRequest {
    private String tripType;  // Family, Friends, Date, Vacation
    private BigDecimal budget;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;  // (e.g., 9:00 AM)
    private LocalTime endTime;    // (e.g., 8:00 PM)

    // Optionally, add time preferences or other criteria
	public String getTripType() {
		return tripType;
	}
	public void setTripType(String tripType) {
		this.tripType = tripType;
	}
	public BigDecimal getBudget() {
		return budget;
	}
	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

    
}
