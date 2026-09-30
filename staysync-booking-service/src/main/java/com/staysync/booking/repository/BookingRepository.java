package com.staysync.booking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.staysync.booking.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(String userId);

    List<Booking> findByRoomId(String roomId);

    // Find confirmed bookings that overlap the requested dates
    List<Booking> findByRoomIdAndStatusAndCheckInLessThanAndCheckOutGreaterThan(
            String roomId,
            String status,
            LocalDate checkOut,
            LocalDate checkIn
    );
}