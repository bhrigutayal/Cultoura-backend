package com.tourism.Cultoura.service;
import com.tourism.Cultoura.RequestModels.EventRequest;
import com.tourism.Cultoura.RequestModels.ItineraryRequest;
import com.tourism.Cultoura.model.ActivityOption;
import com.tourism.Cultoura.model.Event;

import java.util.List;

public interface EventService {
    Event createEvent(EventRequest eventRequest);
    Event updateEvent(Long eventId, EventRequest eventRequest);
    void deleteEvent(Long eventId);
    List<Event> getEventsByCity(String city);
    List<Event> getAllEvents();
    List<Event> getEventsForOrganiser(String organiserUsername);
    List<ActivityOption> getEventOptionsForItinerary(ItineraryRequest request);
}