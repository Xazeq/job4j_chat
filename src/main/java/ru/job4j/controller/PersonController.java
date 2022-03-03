package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Person;
import ru.job4j.domain.Room;
import ru.job4j.service.ChatService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final ChatService service;
    private final RestTemplate restTemplate;
    private final BCryptPasswordEncoder encoder;
    private final ObjectMapper objectMapper;
    private static final String ROOM_API_ID = "http://localhost:8080/room/{id}";

    public PersonController(ChatService service, RestTemplate restTemplate, BCryptPasswordEncoder encoder, ObjectMapper objectMapper) {
        this.service = service;
        this.restTemplate = restTemplate;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return this.service.findAllPersons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        if (id <= 0) {
            throw new NullPointerException("Id should be more than 0");
        }
        Person person = this.service.findPersonById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Person not found. Please, check id."
        ));
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(person);
    }

    @PostMapping("/")
    public ResponseEntity<Person> signUp(@RequestBody Person person) {
        if (person.getUsername() == null || person.getPassword() == null) {
            throw new NullPointerException("Username and password can`t be empty");
        }
        if (person.getUsername().length() > 100 || person.getPassword().length() > 100) {
            throw new IllegalArgumentException("Username or password is too long (max 100 characters)");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        return new ResponseEntity<>(
                this.service.savePerson(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        if (person.getUsername() == null || person.getPassword() == null) {
            throw new NullPointerException("Username and password can`t be empty");
        }
        if (person.getUsername().length() > 100 || person.getPassword().length() > 100) {
            throw new IllegalArgumentException("Username or password is too long (max 100 characters)");
        }
        this.service.savePerson(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (id <= 0) {
            throw new NullPointerException("Id should be more than 0");
        }
        Person person = new Person();
        person.setId(id);
        this.service.deletePerson(person);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{personId}/addRoom/{roomId}")
    public ResponseEntity<Void> addRoomToPerson(@PathVariable int personId, @PathVariable int roomId) {
        if (personId <= 0 || roomId <= 0) {
            throw new NullPointerException("personId or roomId should be more than 0");
        }
        Person person = findById(personId).getBody();
        Room room = service.findRoomById(roomId).orElse(new Room());
        person.addRoom(room);
        this.update(person);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
    }
}
