package com.tourism.Cultoura.service;

import java.math.BigDecimal; 
import org.springframework.stereotype.Service;
import com.tourism.Cultoura.ResponseModels.PaymentDetailsResponse;
import com.tourism.Cultoura.model.ActivityOption;
import com.tourism.Cultoura.model.Itinerary;
import com.tourism.Cultoura.repository.ItineraryRepository;

@Service
public class PaymentService {
	private final ItineraryRepository itineraryRepository;
	
	
    public PaymentService(ItineraryRepository itineraryRepository) {
		this.itineraryRepository = itineraryRepository;
	}
    
    


	public PaymentDetailsResponse getPaymentDetails(Long itineraryId) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
        		  .orElseThrow(() -> new RuntimeException("Booking not found"));

        BigDecimal totalAmount = itinerary.getSections().stream()
            .flatMap(section -> section.getOptions().stream())
            .map(ActivityOption::getCost)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PaymentDetailsResponse(itineraryId, totalAmount, "UPI, Credit/Debit Card, PayPal");
    }
}
