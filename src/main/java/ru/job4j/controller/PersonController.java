package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.domain.Person;
import ru.job4j.domain.Room;
import ru.job4j.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final ChatService service;
    private final RestTemplate restTemplate;
    private static final String ROOM_API_ID = "http://localhost:8080/room/{id}";

    public PersonController(ChatService service, RestTemplate restTemplate) {
        this.service = service;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return this.service.findAllPersons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.service.findPersonById(id);
        return new ResponseEntity<>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return new ResponseEntity<>(
                this.service.savePerson(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        this.service.savePerson(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        this.service.deletePerson(person);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{personId}/addRoom/{roomId}")
    public ResponseEntity<Void> addRoomToPerson(@PathVariable int personId, @PathVariable int roomId) {
        Person person = findById(personId).getBody();
        Room room = restTemplate.getForObject(ROOM_API_ID, Room.class, roomId);
        person.addRoom(room);
        this.update(person);
        return ResponseEntity.ok().build();
    }
}
