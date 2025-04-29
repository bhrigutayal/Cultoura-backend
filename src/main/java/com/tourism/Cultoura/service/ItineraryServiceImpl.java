package com.tourism.Cultoura.service;

import com.tourism.Cultoura.ResourceNotFoundException; 
import com.tourism.Cultoura.DTO.ActivityRequestDTO;
import com.tourism.Cultoura.RequestModels.*;
import com.tourism.Cultoura.ResponseModels.*;
import com.tourism.Cultoura.model.*;
import com.tourism.Cultoura.repository.ActivityItemRepository;
import com.tourism.Cultoura.repository.ItineraryRepository;
import com.tourism.Cultoura.util.ItineraryMapper;
import com.tourism.Cultoura.util.SmartItineraryCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItineraryServiceImpl implements ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final EventService eventService;
    private final ActivityItemRepository activityItemRepo;

    public ItineraryServiceImpl(ItineraryRepository itineraryRepository, EventService eventService,ActivityItemRepository activityItemRepo) {
        this.itineraryRepository = itineraryRepository;
		this.eventService = eventService;
		this.activityItemRepo = activityItemRepo;
    }
    
    public List<ActivitySectionModel> generateActivities(ActivityRequestDTO requestDTO) {
            return createPlan(requestDTO.getPlanType(),requestDTO.getBudget(),requestDTO.getStartTime(),requestDTO.getEndTime(),requestDTO.getDate());
    }
    public List<ActivitySectionModel> createPlan(
            String planType, 
            double budget, 
            String startHour, 
            String endHour,
            String date
        ) {
            // Fetch activities for the specific plan type
            List<ActivityItemModel> fittingActivities = activityItemRepo.findByType(planType);
            
            Map<Integer, List<ActivityItemModel>> groupedBySection = fittingActivities.stream()
            	    .filter(activity ->
            	        activity.getCost() <= budget &&
            	        activity.getDuration() >= Integer.valueOf(startHour) &&
            	        activity.getDuration() <= Integer.valueOf(endHour) &&
            	        (activity.getLocation() == null || activity.getDate().equals(date))
            	    )
            	    .collect(Collectors.groupingBy(ActivityItemModel::getSectionId));

            	List<ActivitySectionModel> result = groupedBySection.entrySet().stream()
            	    .map(entry -> {
            	        ActivitySectionModel section = new ActivitySectionModel();
            	        section.setTitle(getSectionTitleById(entry.getKey())); 
            	        section.setActivities(entry.getValue());
            	        return section;
            	    })
            	    .filter(section -> !section.getActivities().isEmpty())
            	    .collect(Collectors.toList());
            	
            	return result;

        }
    
    private String getSectionTitleById(Integer sectionId) {
        if (sectionId == null) return "Unknown";

        switch (sectionId) {
            case 1: return "Morning Activity";
            case 2: return "Afternoon Activity";
            case 3: return "Evening Activity";
            case 4: return "Night Activity";
            default: return "Unknown";
        }
    }


       

        // Method to optimize activities for best budget utilization
//        public List<ActivitySectionModel> optimizePlan(
//            String planType, 
//            double budget, 
//            int startHour, 
//            int endHour,
//        ) {
//            List<ActivitySectionModel> initialPlan = createPlan(planType, budget, startHour, endHour);
//            
//            return initialPlan.stream()
//                .map(section -> optimizeSectionActivities(section, budget))
//                .collect(Collectors.toList());
//        }

//        private ActivitySectionModel optimizeSectionActivities(
//            ActivitySectionModel section, 
//            double totalBudget
//        ) {
//            List<ActivityItemModel> optimizedActivities = selectOptimalActivities(
//                section.getActivities(), 
//                totalBudget
//            );
//            
//            ActivitySectionModel optimizedSection = new ActivitySectionModel();
//            optimizedSection.setId(section.getId());
//            optimizedSection.setTitle(section.getTitle());
//            optimizedSection.setActivities(optimizedActivities);
//            
//            return optimizedSection;
//        }

        // Implement a simple greedy algorithm for activity selection
//        private List<ActivityItemModel> selectOptimalActivities(
//            List<ActivityItemModel> activities, 
//            double budget
//        ) {
//            // Sort activities by value (rating/cost ratio)
//            return activities.stream()
//                .sorted((a1, a2) -> {
//                    double valueRatio1 = a1.getRating() / (a1.getCost() + 0.01);
//                    double valueRatio2 = a2.getRating() / (a2.getCost() + 0.01);
//                    return Double.compare(valueRatio2, valueRatio1);
//                })
//                .filter(activity -> activity.getCost() <= budget)
//                .collect(Collectors.toList());
//        }
    
    public ItineraryResponse getItineraryById(Long id) {
        Optional<Itinerary> itineraryOptional = itineraryRepository.findById(id);
        if (itineraryOptional.isEmpty()) {
            throw new ResourceNotFoundException("Itinerary with ID " + id + " not found.");
        }
        
        Itinerary itinerary = itineraryOptional.get();
        
        return ItineraryMapper.toDto(itinerary);
    }
    
    public ItineraryResponse finalizeItinerary(Long id, SelectedOptionsRequest request) {
        Itinerary itinerary = itineraryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Itinerary not found"));

        for (SelectedOption option : request.getSelectedOptions()) {
            ItinerarySection section = itinerary.getSections().stream()
                .filter(s -> s.getCategory().equals(option.getCategory()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid category"));

            // Replace options with user selection
            section.setOptions(option.getSelectedActivities());
        }

        itineraryRepository.save(itinerary);
        return ItineraryMapper.toDto(itinerary);
    }

    @Override
    public ItineraryResponse generateItinerary(String username, ItineraryRequest request) {
        Itinerary itinerary = new Itinerary();
        itinerary.setTripType(request.getTripType());
        itinerary.setBudget(request.getBudget());
        itinerary.setStartDate(request.getStartDate());
        itinerary.setEndDate(request.getEndDate());
        itinerary.setCreatedBy(username);

        List<ItinerarySection> sections = new ArrayList<>();

        // Track remaining available time
        LocalTime availableTime = request.getStartTime();
        LocalTime endTime = request.getEndTime();

        // Section 1: Classic Attractions (if time allows)
        List<ActivityOption> classicOptions = SmartItineraryCreator.generateClassicAttractionOptions(request);
        if (!classicOptions.isEmpty()) {
            ItinerarySection classicSection = new ItinerarySection();
            classicSection.setCategory(""); // To be filled with the type of activities we list in this section 
            classicSection.setItinerary(itinerary);
            classicSection.setOptions(classicOptions);
            sections.add(classicSection);

            availableTime = classicOptions.get(classicOptions.size() - 1).getEndTime().plusHours(1);
        }

        // Section 2: Cultural Events (if time remains)
        if (availableTime.isBefore(endTime)) {
            List<ActivityOption> eventOptions = eventService.getEventOptionsForItinerary(request);
            if (!eventOptions.isEmpty()) {
                ItinerarySection culturalSection = new ItinerarySection();
                culturalSection.setCategory(""); // To be filled with the type of activities we list in this section
                culturalSection.setItinerary(itinerary);
                culturalSection.setOptions(eventOptions);
                sections.add(culturalSection);
            }
        }

        itinerary.setSections(sections);
        Itinerary savedItinerary = itineraryRepository.save(itinerary);
        return ItineraryMapper.toDto(savedItinerary);
    }




}
