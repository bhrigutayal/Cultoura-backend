package com.tourism.Cultoura.RequestModels;

import java.util.List;

import com.tourism.Cultoura.model.ActivityOption;

public class SelectedOption {
    private String category; // Example: "Outdoor Activities", "Food & Dining"
    private List<ActivityOption> selectedActivities;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<ActivityOption> getSelectedActivities() {
		return selectedActivities;
	}
	public void setSelectedActivities(List<ActivityOption> selectedActivities) {
		this.selectedActivities = selectedActivities;
	}
}
