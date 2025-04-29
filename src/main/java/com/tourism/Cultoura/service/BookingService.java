package com.tourism.Cultoura.service;

import com.tourism.Cultoura.RequestModels.BookingRequest;
import com.tourism.Cultoura.RequestModels.PaymentRequest;
import com.tourism.Cultoura.ResponseModels.BookingConfirmationResponse;
import com.tourism.Cultoura.model.Booking;

public interface BookingService {
    Booking createBooking(Long eventId, BookingRequest bookingRequest);
    Booking getBooking(Long bookingId);
    void cancelBooking(Long bookingId);
    public BookingConfirmationResponse confirmBooking(Long bookingId, PaymentRequest paymentRequest);
}