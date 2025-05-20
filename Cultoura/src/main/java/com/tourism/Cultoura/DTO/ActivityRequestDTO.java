package com.tourism.Cultoura.DTO;
import com.tourism.Cultoura.util.ActivityConstants;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActivityRequestDTO {
    @JsonProperty(ActivityConstants.PLAN_TYPE)
    private String planType;

    @JsonProperty(ActivityConstants.DATE)
    private String date;

    @JsonProperty(ActivityConstants.START_TIME)
    private String startTime;

    @JsonProperty(ActivityConstants.END_TIME)
    private String endTime;

    @JsonProperty(ActivityConstants.BUDGET)
    private Double budget;
    
    @JsonProperty(ActivityConstants.CITY)
    private String city;
    
    
    

    // Default constructor
    public ActivityRequestDTO() {}

    // Constructor with all fields
    public ActivityRequestDTO(String planType, String date, String startTime, String endTime, Double budget, String city) {
        this.planType = planType;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.budget = budget;
        this.city = city;
    }

    // Getters and setters
    public String getPlanType() { return planType; }
    public void setPlanType(String planType) { this.planType = planType; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }

	@Override
	public String toString() {
		return "ActivityRequestDTO [planType=" + planType + ", date=" + date + ", startTime=" + startTime + ", endTime="
				+ endTime + ", budget=" + budget + "]";
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}