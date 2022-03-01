package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Message;
import ru.job4j.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final ChatService service;

    public MessageController(ChatService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Message> findAll() {
        return this.service.findAllMessages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        var message = this.service.findMessageById(id);
        return new ResponseEntity<>(
                message.orElse(new Message()),
                message.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        return new ResponseEntity<>(
                this.service.saveMessage(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        this.service.saveMessage(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Message message = new Message();
        message.setId(id);
        this.service.deleteMessage(message);
        return ResponseEntity.ok().build();
    }
}
