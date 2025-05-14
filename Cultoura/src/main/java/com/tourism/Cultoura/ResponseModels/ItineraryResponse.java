package com.tourism.Cultoura.ResponseModels;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ItineraryResponse {
    private Long id;
    private String tripType;
    private BigDecimal budget;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ItinerarySectionDTO> sections;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public List<ItinerarySectionDTO> getSections() {
		return sections;
	}
	public void setSections(List<ItinerarySectionDTO> sections) {
		this.sections = sections;
	}
    
}
