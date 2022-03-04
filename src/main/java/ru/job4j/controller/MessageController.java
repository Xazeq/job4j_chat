package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.service.ChatService;

import java.lang.reflect.InvocationTargetException;
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
        if (id <= 0) {
            throw new NullPointerException("Id should be more than 0");
        }
        Message message = this.service.findMessageById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Message not found. Please, check id."
        ));
        return new ResponseEntity<>(
                message,
                HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        if (message.getText() == null) {
            throw new NullPointerException("Message text can`t be empty");
        }
        return new ResponseEntity<>(
                this.service.saveMessage(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        if (message.getText() == null) {
            throw new NullPointerException("Message text can`t be empty");
        }
        this.service.saveMessage(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (id <= 0) {
            throw new NullPointerException("Id should be more than 0");
        }
        Message message = new Message();
        message.setId(id);
        this.service.deleteMessage(message);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    public ResponseEntity<Message> partialUpdateMessage(@RequestBody Message message)
            throws InvocationTargetException, IllegalAccessException {
        return new ResponseEntity<>(
                this.service.partialUpdateMessage(message),
                HttpStatus.OK
        );
    }
}
