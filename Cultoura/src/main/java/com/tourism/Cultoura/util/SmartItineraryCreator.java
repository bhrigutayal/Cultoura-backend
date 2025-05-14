package com.tourism.Cultoura.util;

import com.tourism.Cultoura.RequestModels.ItineraryRequest;
import com.tourism.Cultoura.model.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SmartItineraryCreator {

    public static List<ItinerarySection> generateSections(Itinerary itinerary) {
        List<ItinerarySection> sections = new ArrayList<>();

        // For example, generate two sections with dummy options:
        for (int i = 1; i <= 2; i++) {
            ItinerarySection section = new ItinerarySection();
            section.setCategory("");
            section.setItinerary(itinerary);

            // Generate a few activity options for this section
            List<ActivityOption> options = new ArrayList<>();
            for (int j = 1; j <= 3; j++) {
                ActivityOption option = new ActivityOption();
                option.setName("Activity Option " + j + " for Section " + i);
                option.setDescription("Description for activity option " + j);
                option.setCost(new java.math.BigDecimal("50.00"));
                option.setStartTime(LocalTime.of(10 + j, 0));
                option.setEndTime(LocalTime.of(11 + j, 0));
                option.setSection(section);
                options.add(option);
            }
            section.setOptions(options);
            sections.add(section);
        }
        return sections;
    }
    public static List<ActivityOption> generateClassicAttractionOptions(ItineraryRequest request) {
        List<ActivityOption> options = new ArrayList<>();
        LocalTime availableTime = request.getStartTime();
        LocalTime endTime = request.getEndTime();

        int durationPerActivity = 2; // Assume each activity takes 2 hours

        while (availableTime.plusHours(durationPerActivity).isBefore(endTime)) {
            ActivityOption option = new ActivityOption();
            option.setName("Classic Attraction: " + availableTime);
            option.setDescription("Famous attraction visit.");
            option.setCost(new BigDecimal("30.00"));
            option.setStartTime(availableTime);
            option.setEndTime(availableTime.plusHours(durationPerActivity));

            options.add(option);

            availableTime = availableTime.plusHours(durationPerActivity + 1); // Add a 1-hour buffer
        }

        return options;
    }
}
