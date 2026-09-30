package com.staysync.staysyncroomservice.repository;

import com.staysync.staysyncroomservice.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomNumber(String roomNumber);

    List<Room> findByStatusIgnoreCase(String status);

    List<Room> findByCapacityGreaterThanEqual(Integer capacity);
}