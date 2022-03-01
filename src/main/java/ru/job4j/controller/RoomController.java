package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.service.ChatService;

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
        var room = this.service.findRoomById(id);
        return new ResponseEntity<>(
                room.orElse(new Room()),
                room.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        return new ResponseEntity<>(
                this.service.saveRoom(room),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
        this.service.saveRoom(room);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Room room = new Room();
        room.setId(id);
        this.service.deleteRoom(room);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roomId}/addMessage")
    public ResponseEntity<Void> addMessageToRoom(@PathVariable int roomId, @RequestBody Message message) {
        Room room = findById(roomId).getBody();
        Message savedMessage = restTemplate.postForObject(MESSAGE_API, message, Message.class);
        room.addMessage(savedMessage);
        this.update(room);
        return ResponseEntity.ok().build();
    }
}
