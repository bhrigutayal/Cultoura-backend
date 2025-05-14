package com.tourism.Cultoura.ResponseModels;

import java.util.List;

public class ItinerarySectionDTO {
    private String category;
    private List<ActivityOptionDTO> options;
	public String getCategory() {
		return category;
	}
	public void setCategory(String sectionNumber) {
		this.category = sectionNumber;
	}
	public List<ActivityOptionDTO> getOptions() {
		return options;
	}
	public void setOptions(List<ActivityOptionDTO> options) {
		this.options = options;
	}
}

