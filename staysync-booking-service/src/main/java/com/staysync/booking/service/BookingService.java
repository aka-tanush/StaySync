package com.staysync.booking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.staysync.booking.entity.Booking;
import com.staysync.booking.repository.BookingRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Create booking
    public Booking createBooking(Booking booking) {

        // Basic date validation
        if (booking.getCheckIn() == null || booking.getCheckOut() == null) {
            throw new IllegalArgumentException(
                    "Check-in and check-out dates are required"
            );
        }

        if (!booking.getCheckOut().isAfter(booking.getCheckIn())) {
            throw new IllegalArgumentException(
                    "Check-out date must be after check-in date"
            );
        }

        // Room validation
        if (booking.getRoomId() == null || booking.getRoomId().isBlank()) {
            throw new IllegalArgumentException(
                    "Room ID is required"
            );
        }

        // Default status
        if (booking.getStatus() == null || booking.getStatus().isBlank()) {
            booking.setStatus("CONFIRMED");
        }

        // Check whether the room is already booked for these dates
        List<Booking> overlappingBookings =
                bookingRepository
                        .findByRoomIdAndStatusAndCheckInLessThanAndCheckOutGreaterThan(
                                booking.getRoomId(),
                                "CONFIRMED",
                                booking.getCheckOut(),
                                booking.getCheckIn()
                        );

        if (!overlappingBookings.isEmpty()) {
            throw new IllegalArgumentException(
                    "Room " + booking.getRoomNumber()
                            + " is already booked for the selected dates"
            );
        }

        // Generate booking reference
        if (booking.getBookingReference() == null
                || booking.getBookingReference().isBlank()) {

            booking.setBookingReference(
                    "STY-" + UUID.randomUUID()
                            .toString()
                            .substring(0, 8)
                            .toUpperCase()
            );
        }

        // Set creation time
        if (booking.getCreatedAt() == null) {
            booking.setCreatedAt(LocalDateTime.now());
        }

        return bookingRepository.save(booking);
    }

    // Get booking by ID
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get bookings by user
    public List<Booking> getBookingsByUser(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    // Get bookings by room
    public List<Booking> getBookingsByRoom(String roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    // Cancel booking
    public Booking cancelBooking(Long id) {

        Booking booking = bookingRepository.findById(id).orElse(null);

        if (booking == null) {
            return null;
        }

        booking.setStatus("CANCELLED");

        return bookingRepository.save(booking);
    }
}