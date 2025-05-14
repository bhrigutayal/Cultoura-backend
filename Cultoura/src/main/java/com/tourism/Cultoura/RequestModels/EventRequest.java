package com.tourism.Cultoura.RequestModels;

import java.time.LocalDate;

public class EventRequest {
    private String title;
    private String description;
    private String city;
    private LocalDate date;
    private String organisedBy;

    

	public EventRequest() {}

    // Getters and Setters
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getOrganisedBy() {
		return organisedBy;
	}

	public void setOrganisedBy(String organisedBy) {
		this.organisedBy = organisedBy;
	}
}