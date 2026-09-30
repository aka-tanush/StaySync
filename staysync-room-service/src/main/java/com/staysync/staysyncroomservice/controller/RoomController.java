package com.staysync.staysyncroomservice.controller;

import com.staysync.staysyncroomservice.entity.Room;
import com.staysync.staysyncroomservice.service.RoomService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Get all rooms
    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    // Get room by ID
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    // Get room by room number
    @GetMapping("/number/{roomNumber}")
    public ResponseEntity<Room> getRoomByNumber(
            @PathVariable String roomNumber) {

        return ResponseEntity.ok(
                roomService.getRoomByNumber(roomNumber)
        );
    }

    // Get rooms by status
    @GetMapping("/status/{status}")
    public List<Room> getRoomsByStatus(
            @PathVariable String status) {

        return roomService.getRoomsByStatus(status);
    }

    // Get rooms by minimum capacity
    @GetMapping("/capacity/{capacity}")
    public List<Room> getRoomsByCapacity(
            @PathVariable Integer capacity) {

        return roomService.getRoomsByCapacity(capacity);
    }

    // Create room
    @PostMapping
    public ResponseEntity<Room> createRoom(
            @RequestBody Room room) {

        return ResponseEntity.ok(
                roomService.createRoom(room)
        );
    }

    // Update room
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long id,
            @RequestBody Room room) {

        return ResponseEntity.ok(
                roomService.updateRoom(id, room)
        );
    }

    // Delete room
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long id) {

        roomService.deleteRoom(id);

        return ResponseEntity.noContent().build();
    }

    // Update room status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Room> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                roomService.updateStatus(id, status)
        );
    }
}