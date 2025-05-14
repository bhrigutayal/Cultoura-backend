package com.tourism.Cultoura.controller;

import com.tourism.Cultoura.RequestModels.ItineraryRequest;  
import com.tourism.Cultoura.RequestModels.PaymentRequest;
import com.tourism.Cultoura.RequestModels.SelectedOptionsRequest;
import com.tourism.Cultoura.ResponseModels.BookingConfirmationResponse;
import com.tourism.Cultoura.ResponseModels.ItineraryResponse;
import com.tourism.Cultoura.ResponseModels.PaymentDetailsResponse;
import com.tourism.Cultoura.DTO.ActivityRequestDTO;
import com.tourism.Cultoura.model.ActivitySectionModel;
import com.tourism.Cultoura.service.BookingService;
import com.tourism.Cultoura.service.ItineraryService;
import com.tourism.Cultoura.service.PaymentService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/itineraries")
public class ItineraryController {

    private final ItineraryService itineraryService;
    private final PaymentService paymentService;
    private final BookingService bookingService;

    public ItineraryController(ItineraryService itineraryService, PaymentService paymentService,
			BookingService bookingService) {
		this.itineraryService = itineraryService;
		this.paymentService = paymentService;
		this.bookingService = bookingService;
	}

    @PostMapping("/generate")
    public ResponseEntity<ItineraryResponse> generateItinerary(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ItineraryRequest request) {
        ItineraryResponse response = itineraryService.generateItinerary(userDetails.getUsername(), request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}/select-options")
    public ResponseEntity<ItineraryResponse> selectOptions(@PathVariable Long id, @RequestBody SelectedOptionsRequest request) {
        ItineraryResponse response = itineraryService.finalizeItinerary(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/payment-details")
    public ResponseEntity<PaymentDetailsResponse> getPaymentDetails(@PathVariable Long id) {
        PaymentDetailsResponse response = paymentService.getPaymentDetails(id);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}/confirm-booking")
    public ResponseEntity<BookingConfirmationResponse> confirmBooking(@PathVariable Long id, @RequestBody PaymentRequest request) {
        BookingConfirmationResponse response = bookingService.confirmBooking(id, request);
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/activity-choices")
    public List<ActivitySectionModel> generateActivities(@RequestBody ActivityRequestDTO requestDTO) {
        return itineraryService.generateActivities(requestDTO);
    }





    @GetMapping("/{id}")
    public ResponseEntity<ItineraryResponse> getItinerary(@PathVariable Long id) {
        ItineraryResponse response = itineraryService.getItineraryById(id);
        return ResponseEntity.ok(response);
    }


}
