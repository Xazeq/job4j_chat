package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.service.ChatService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final ChatService service;
    private final RestTemplate restTemplate;
    private static final String MESSAGE_API = "http://localhost:8080/message/";

    public RoomController(ChatService service, RestTemplate restTemplate) {
        this.service = service;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public List<Room> findAll() {
        return this.service.findAllRooms();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int id) {
        if (id <= 0) {
            throw new NullPointerException("Id should be more than 0");
        }
        Room room = this.service.findRoomById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Room not found. Please, check id."
        ));
        return new ResponseEntity<>(
                room,
                HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        if (room.getName() == null) {
            throw new NullPointerException("Room name can`t be empty");
        }
        return new ResponseEntity<>(
                this.service.saveRoom(room),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
        if (room.getName() == null) {
            throw new NullPointerException("Room name can`t be empty");
        }
        this.service.saveRoom(room);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (id <= 0) {
            throw new NullPointerException("Id should be more than 0");
        }
        Room room = new Room();
        room.setId(id);
        this.service.deleteRoom(room);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roomId}/addMessage")
    public ResponseEntity<Void> addMessageToRoom(@PathVariable int roomId, @RequestBody Message message) {
        if (roomId <= 0) {
            throw new NullPointerException("roomId should be more than 0");
        }
        if (message.getText() == null) {
            throw new NullPointerException("Message text can`t be empty");
        }
        Room room = findById(roomId).getBody();
        Message savedMessage = service.saveMessage(message);
        room.addMessage(savedMessage);
        this.update(room);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    public ResponseEntity<Room> partialUpdateRoom(@RequestBody Room room)
            throws InvocationTargetException, IllegalAccessException {
        return new ResponseEntity<>(
                this.service.partialUpdateRoom(room),
                HttpStatus.OK
        );
    }
}
