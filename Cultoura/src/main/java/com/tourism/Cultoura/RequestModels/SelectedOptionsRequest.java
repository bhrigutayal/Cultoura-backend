package com.tourism.Cultoura.RequestModels;

import java.util.List;

public class SelectedOptionsRequest {
    private List<SelectedOption> selectedOptions;

	public List<SelectedOption> getSelectedOptions() {
		return selectedOptions;
	}

	public void setSelectedOptions(List<SelectedOption> selectedOptions) {
		this.selectedOptions = selectedOptions;
	}
}