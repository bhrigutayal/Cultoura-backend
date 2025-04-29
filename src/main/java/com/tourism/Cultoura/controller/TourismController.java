package com.tourism.Cultoura.controller;
import com.tourism.Cultoura.RequestModels.BookingRequest;
import com.tourism.Cultoura.RequestModels.EventRequest;
import com.tourism.Cultoura.RequestModels.LoginRequest;
import com.tourism.Cultoura.RequestModels.RegistrationRequest;
import com.tourism.Cultoura.jwt.JwtTokenResponse;
import com.tourism.Cultoura.model.Booking;
import com.tourism.Cultoura.model.Event;
import com.tourism.Cultoura.service.AuthService;
import com.tourism.Cultoura.service.BookingService;
import com.tourism.Cultoura.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.util.List;

@RestController
@RequestMapping("/api")
public class TourismController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EventService eventService;

    @Autowired
    private BookingService bookingService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        authService.register(registrationRequest);
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String jwtToken = authService.authenticate(loginRequest);
        return ResponseEntity.ok(new JwtTokenResponse(jwtToken));
    }

   
    @PreAuthorize("hasRole('EVENT_ORGANISER')")
    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody EventRequest eventRequest) {
        Event createdEvent = eventService.createEvent(eventRequest);
        return ResponseEntity.ok(createdEvent);
    }
    @PreAuthorize("hasRole('EVENT_ORGANISER')")
    @GetMapping("/organiser/events")
    public ResponseEntity<List<Event>> getOrganiserEvents(Authentication authentication) {
        // Extract the organiser's username (or identifier) from the authentication object.
        String organiserUsername = authentication.getName();
        
        // Retrieve events for the organiser
        List<Event> events = eventService.getEventsForOrganiser(organiserUsername);
        
        return ResponseEntity.ok(events);
    }

    // For event organisers: Update an existing event.
    @PreAuthorize("hasRole('EVENT_ORGANISER')")
    @PutMapping("/events/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable("eventId") Long eventId, @RequestBody EventRequest eventRequest) {
        Event updatedEvent = eventService.updateEvent(eventId, eventRequest);
        return ResponseEntity.ok(updatedEvent);
    }

    // For event organisers: Delete an event.
    @PreAuthorize("hasRole('EVENT_ORGANISER')")
    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable("eventId") Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok("Event deleted successfully");
    }

    // Public endpoint: List events, with optional filtering by city.
    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEvents(@RequestParam(name = "city", required = false)  String city) {
        List<Event> events;
        if (city != null && !city.isEmpty()) {
            events = eventService.getEventsByCity(city);
        } else {
            events = eventService.getAllEvents();
        }
        return ResponseEntity.ok(events);
    }

    // ----------------------------
    // Booking Endpoints
    // ----------------------------

    // For users: Book an event.
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/events/{eventId}/bookings")
    public ResponseEntity<Booking> bookEvent(@PathVariable("eventId") Long eventId, @RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(eventId, bookingRequest);
        return ResponseEntity.ok(booking);
    }

    // For users: Retrieve booking details.
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable("bookingId") Long bookingId) {
        Booking booking = bookingService.getBooking(bookingId);
        return ResponseEntity.ok(booking);
    }

    // For users: Cancel a booking.
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable("bookingId") Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("Booking cancelled successfully");
    }
}
