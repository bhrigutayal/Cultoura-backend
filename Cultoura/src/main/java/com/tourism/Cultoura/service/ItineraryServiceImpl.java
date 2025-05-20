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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static final Logger logger = LoggerFactory.getLogger(ItineraryServiceImpl.class);
    public ItineraryServiceImpl(ItineraryRepository itineraryRepository, EventService eventService,ActivityItemRepository activityItemRepo) {
        this.itineraryRepository = itineraryRepository;
		this.eventService = eventService;
		this.activityItemRepo = activityItemRepo;
    }
    
    public List<ActivitySectionModel> generateActivities(ActivityRequestDTO requestDTO) {
    	    logger.debug("Request: {}", requestDTO);
            return createPlan(requestDTO.getPlanType(),requestDTO.getBudget(),requestDTO.getStartTime(),requestDTO.getEndTime(),requestDTO.getDate(), requestDTO.getCity());
    }
    public List<ActivitySectionModel> createPlan(
            String planType, 
            double budget, 
            String startHour, 
            String endHour,
            String date,
            String city
        ) {
            // Fetch activities for the specific plan type
            List<ActivityItemModel> fittingActivities = activityItemRepo.findByCityAndType(city,planType);
            long allActivities = activityItemRepo.count();
            logger.debug("Fitting Activities: {}, All Activities: {}", fittingActivities, allActivities);
            
            
         // First, filter activities that meet basic criteria
            List<ActivityItemModel> eligibleActivities = fittingActivities.stream()
                .filter(activity -> {
                    // Budget check
                    boolean withinBudget = activity.getCost() <= budget;
                    
                    // Date check
                    boolean dateMatches = true;
                    if (date != null && !date.isEmpty()) {
                        dateMatches = activity.getDate() == null || activity.getDate().isEmpty() || 
                                      date.equals(activity.getDate());
                    }
                    
                    boolean reasonableDuration = timeWindowContainsActivity(startHour,endHour,activity.getStartHour(),activity.getEndHour());
                    return withinBudget && dateMatches && reasonableDuration;
                })
                .collect(Collectors.toList());

            // Parse user time window
            LocalTime startTime = (startHour != null) ? LocalTime.parse(startHour) : LocalTime.of(6, 0);
            LocalTime endTime = (endHour != null) ? LocalTime.parse(endHour) : LocalTime.of(22, 0);

            // Define time boundaries based on standard day parts
            LocalTime morningStart = LocalTime.of(6, 0);
            LocalTime afternoonStart = LocalTime.of(12, 0);
            LocalTime eveningStart = LocalTime.of(17, 0);
            LocalTime nightEnd = LocalTime.of(22, 0);

            // Calculate overlap with day parts and group activities accordingly
            Map<Integer, List<ActivityItemModel>> groupedBySection = new HashMap<>();

            // Initialize sections
            groupedBySection.put(0, new ArrayList<>()); // Morning
            groupedBySection.put(1, new ArrayList<>()); // Afternoon
            groupedBySection.put(2, new ArrayList<>()); // Evening

            // Distribute activities to appropriate sections
            for (ActivityItemModel activity : eligibleActivities) {
                // Morning section (if user window overlaps with morning)
                if (!startTime.isAfter(afternoonStart) && !endTime.isBefore(morningStart)) {
                    groupedBySection.get(0).add(activity);
                }
                // Afternoon section (if user window overlaps with afternoon)
                else if (!startTime.isAfter(eveningStart) && !endTime.isBefore(afternoonStart)) {
                    groupedBySection.get(1).add(activity);
                }
                // Evening section (if user window overlaps with evening)
                else if (!startTime.isAfter(nightEnd) && !endTime.isBefore(eveningStart)) {
                    groupedBySection.get(2).add(activity);
                }
            }
            List<ActivitySectionModel> result = new ArrayList<>();
        
            result.add(new ActivitySectionModel(0L,getSectionTitleById(0),groupedBySection.get(0)));
            result.add(new ActivitySectionModel(1L,getSectionTitleById(1),groupedBySection.get(1)));
            result.add(new ActivitySectionModel(2L,getSectionTitleById(2),groupedBySection.get(2)));
            
            	
            	return result;

        }
    
    private String getSectionTitleById(Integer sectionId) {
        if (sectionId == null) return "Unknown";

        switch (sectionId) {
            case 0: return "Morning Activity";
            case 1: return "Afternoon Activity";
            case 2: return "Evening Activity";
            case 3: return "Night Activity";
            default: return "Unknown";
        }
    }

    boolean timeWindowContainsActivity(String userStartHour, String userEndHour, 
            String activityStartHour, String activityEndHour) {
try {
LocalTime userStart = LocalTime.parse(userStartHour);
LocalTime userEnd = LocalTime.parse(userEndHour);
LocalTime activityStart = LocalTime.parse(activityStartHour);
LocalTime activityEnd = LocalTime.parse(activityEndHour);

// Check if user's time window completely contains the activity's time window
return (userStart.compareTo(activityStart) <= 0 && userEnd.compareTo(activityEnd) >= 0);
} catch (DateTimeParseException e) {
// Handle parsing errors
return false;
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
