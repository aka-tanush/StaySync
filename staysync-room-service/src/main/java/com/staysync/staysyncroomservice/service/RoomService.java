package com.staysync.staysyncroomservice.service;

import com.staysync.staysyncroomservice.entity.Room;
import com.staysync.staysyncroomservice.repository.RoomRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Get all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Get room by ID
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Room not found with ID: " + id));
    }

    // Get room by room number
    public Room getRoomByNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() ->
                        new RuntimeException("Room not found: " + roomNumber));
    }

    // Get rooms by status
    public List<Room> getRoomsByStatus(String status) {
        return roomRepository.findByStatusIgnoreCase(status);
    }

    // Get rooms by minimum capacity
    public List<Room> getRoomsByCapacity(Integer capacity) {
        return roomRepository.findByCapacityGreaterThanEqual(capacity);
    }

    // Create room
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    // Update room
    public Room updateRoom(Long id, Room roomDetails) {

        Room room = getRoomById(id);

        room.setRoomNumber(roomDetails.getRoomNumber());
        room.setRoomType(roomDetails.getRoomType());
        room.setFloor(roomDetails.getFloor());
        room.setCapacity(roomDetails.getCapacity());
        room.setNightlyRate(roomDetails.getNightlyRate());
        room.setStatus(roomDetails.getStatus());

        return roomRepository.save(room);
    }

    // Delete room
    public void deleteRoom(Long id) {
        Room room = getRoomById(id);
        roomRepository.delete(room);
    }

    // Update room status
    public Room updateStatus(Long id, String status) {

        Room room = getRoomById(id);
        room.setStatus(status);

        return roomRepository.save(room);
    }
}