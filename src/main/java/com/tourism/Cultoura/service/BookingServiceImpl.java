package com.tourism.Cultoura.service;

import com.tourism.Cultoura.ResourceNotFoundException; 
import com.tourism.Cultoura.RequestModels.BookingRequest;
import com.tourism.Cultoura.RequestModels.PaymentRequest;
import com.tourism.Cultoura.ResponseModels.BookingConfirmationResponse;
import com.tourism.Cultoura.model.Booking;
import com.tourism.Cultoura.model.Event;
import com.tourism.Cultoura.model.Itinerary;
import com.tourism.Cultoura.repository.BookingRepository;
import com.tourism.Cultoura.repository.EventRepository;
import com.tourism.Cultoura.repository.ItineraryRepository;

import jakarta.transaction.Transactional;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final ItineraryRepository itineraryRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, EventRepository eventRepository , ItineraryRepository itineraryRepository) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
        this.itineraryRepository = itineraryRepository;
    }

    @Override
    public Booking createBooking(Long eventId, BookingRequest bookingRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        Booking booking = new Booking();
        booking.setEvent(event);
        booking.setUserName(bookingRequest.getUserName());
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBooking(Long bookingId) {
        Booking booking  = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return booking;
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }
    @Transactional
    public BookingConfirmationResponse confirmBooking(Long bookingId, PaymentRequest paymentRequest) {
        // Fetch the booking details
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            throw new ResourceNotFoundException("Booking with ID " + bookingId + " not found.");
        }
        Booking booking = bookingOptional.get();

        // Validate that the itinerary exists
        Optional<Itinerary> itineraryOptional = itineraryRepository.findById(booking.getItineraryId());
        if (itineraryOptional.isEmpty()) {
            throw new ResourceNotFoundException("Itinerary for booking ID " + bookingId + " not found.");
        }
        //Itinerary itinerary = itineraryOptional.get();

        // Ensure the booking is not already confirmed
        if (booking.isConfirmed()) {
            throw new IllegalStateException("Booking is already confirmed.");
        }
        return new BookingConfirmationResponse();
        // Process payment
//        PaymentDetailsResponse paymentResponse = paymentService.processPayment(paymentRequest);
//
//        // If payment is successful, confirm the booking
//        if (paymentResponse.isPaymentSuccessful()) {
//            booking.setConfirmed(true);
//            booking.setPaymentStatus("PAID");
//            booking.setTransactionId(paymentResponse.getTransactionId());
//            bookingRepository.save(booking);
//
//            return new BookingConfirmationResponse(
//                booking.getId(),
//                itinerary.getId(),
//                itinerary.getUserId(),
//                paymentResponse.getTransactionId(),
//                booking.getTotalCost(),
//                "CONFIRMED"
//            );
//        } else {
//            throw new RuntimeException("Payment failed: " + paymentResponse.getFailureReason());
//        }
    }
}
