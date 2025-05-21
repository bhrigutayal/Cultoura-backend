package com.tourism.Cultoura.DTO;

import java.util.List;

import com.tourism.Cultoura.model.ActivitySectionModel;

public class ActivityResponseDTO {
	private List<ActivitySectionModel> sections;

	public List<ActivitySectionModel> getSections() {
		return sections;
	}

	public void setSections(List<ActivitySectionModel> sections) {
		this.sections = sections;
	}

	public ActivityResponseDTO(List<ActivitySectionModel> sections) {
		super();
		this.sections = sections;
	}
}
