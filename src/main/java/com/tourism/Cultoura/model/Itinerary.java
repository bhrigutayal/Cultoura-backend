package com.tourism.Cultoura.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "itineraries")
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // For example, use a string or an enum value: DATE, FRIENDS, FAMILY, VACATION
    private String tripType;

    private BigDecimal budget;

    private LocalDate startDate;
    private LocalDate endDate;

    // This could be the user identifier (for example, the organiser or the user planning the trip)
    private String createdBy;

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItinerarySection> sections;
    private boolean isFinalized; // True if user has confirmed

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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<ItinerarySection> getSections() {
		return sections;
	}

	public void setSections(List<ItinerarySection> sections) {
		this.sections = sections;
	}

	public boolean isFinalized() {
		return isFinalized;
	}

	public void setFinalized(boolean isFinalized) {
		this.isFinalized = isFinalized;
	}

  
}
