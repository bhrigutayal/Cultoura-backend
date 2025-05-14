package com.tourism.Cultoura.ResponseModels;

import java.math.BigDecimal;

public class BillItem {
    private String activityName;
    private String category; // Example: "Outdoor Activity", "Dining"
    private BigDecimal cost;
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
}