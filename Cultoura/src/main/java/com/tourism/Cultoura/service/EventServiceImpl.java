package com.tourism.Cultoura.service;

import com.tourism.Cultoura.RequestModels.EventRequest;
import com.tourism.Cultoura.RequestModels.ItineraryRequest;
import com.tourism.Cultoura.model.ActivityOption;
import com.tourism.Cultoura.model.Event;
import com.tourism.Cultoura.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    
    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    
    @Override
    public List<ActivityOption> getEventOptionsForItinerary(ItineraryRequest request) {
        List<ActivityOption> options = new ArrayList<>();
        LocalTime availableTime = request.getStartTime();
        LocalTime endTime = request.getEndTime();

        int eventDuration = 3; // Assume each cultural event lasts 3 hours

        while (availableTime.plusHours(eventDuration).isBefore(endTime)) {
            ActivityOption option = new ActivityOption();
            option.setName("Cultural Event: " + availableTime);
            option.setDescription("Enjoy an immersive cultural event.");
            option.setCost(new BigDecimal("40.00"));
            option.setStartTime(availableTime);
            option.setEndTime(availableTime.plusHours(eventDuration));

            options.add(option);

            availableTime = availableTime.plusHours(eventDuration + 1); // 1-hour buffer time
        }

        return options;
    }

    @Override
    public Event createEvent(EventRequest eventRequest) {
        Event event = new Event();
        event.setTitle(eventRequest.getTitle());
        event.setDescription(eventRequest.getDescription());
        event.setCity(eventRequest.getCity());
        event.setDate(eventRequest.getDate());
        event.setOrganisedBy(eventRequest.getOrganisedBy());
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long eventId, EventRequest eventRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setTitle(eventRequest.getTitle());
        event.setDescription(eventRequest.getDescription());
        event.setCity(eventRequest.getCity());
        event.setDate(eventRequest.getDate());
        event.setOrganisedBy(eventRequest.getOrganisedBy());
        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    @Override
    public List<Event> getEventsByCity(String city) {
        return eventRepository.findByCityIgnoreCase(city);
    }
    @Override
    public List<Event> getEventsForOrganiser(String organiserUsername) {
        return eventRepository.findByOrganisedBy(organiserUsername);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}
