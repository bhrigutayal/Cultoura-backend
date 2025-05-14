package com.tourism.Cultoura.util;

import com.tourism.Cultoura.ResponseModels.*;
import com.tourism.Cultoura.model.*;
import java.util.List;
import java.util.stream.Collectors;

public class ItineraryMapper {

    public static ItineraryResponse toDto(Itinerary itinerary) {
        ItineraryResponse dto = new ItineraryResponse();
        dto.setId(itinerary.getId());
        dto.setTripType(itinerary.getTripType());
        dto.setBudget(itinerary.getBudget());
        dto.setStartDate(itinerary.getStartDate());
        dto.setEndDate(itinerary.getEndDate());
        dto.setSections(toSectionDtoList(itinerary.getSections()));
        return dto;
    }

    private static List<ItinerarySectionDTO> toSectionDtoList(List<ItinerarySection> sections) {
        return sections.stream().map(section -> {
            ItinerarySectionDTO dto = new ItinerarySectionDTO();
            dto.setCategory(section.getCategory());
            dto.setOptions(section.getOptions().stream().map(option -> {
                ActivityOptionDTO aDto = new ActivityOptionDTO();
                aDto.setId(option.getId());
                aDto.setName(option.getName());
                aDto.setDescription(option.getDescription());
                aDto.setCost(option.getCost());
                aDto.setStartTime(option.getStartTime());
                aDto.setEndTime(option.getEndTime());
                return aDto;
            }).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }
}
