package com.tourism.Cultoura.service;

import java.util.List; 

import com.tourism.Cultoura.RequestModels.ItineraryRequest;
import com.tourism.Cultoura.RequestModels.SelectedOptionsRequest;
import com.tourism.Cultoura.ResponseModels.ItineraryResponse;
import com.tourism.Cultoura.DTO.ActivityRequestDTO;
import com.tourism.Cultoura.model.ActivitySectionModel;

public interface ItineraryService {
    ItineraryResponse generateItinerary(String username, ItineraryRequest request);
    ItineraryResponse finalizeItinerary(Long id, SelectedOptionsRequest request);
    ItineraryResponse getItineraryById(Long id) ;
    List<ActivitySectionModel> generateActivities(ActivityRequestDTO requestDTO);
}
