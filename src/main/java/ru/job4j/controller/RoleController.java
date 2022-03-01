package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Role;
import ru.job4j.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    private final ChatService service;

    public RoleController(ChatService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Role> findAll() {
        return this.service.findAllRoles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable int id) {
        var role = service.findRoleById(id);
        return new ResponseEntity<>(
                role.orElse(new Role()),
                role.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Role> create(@RequestBody Role role) {
        return new ResponseEntity<>(
                this.service.saveRole(role),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Role role) {
        this.service.saveRole(role);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Role role = new Role();
        role.setId(id);
        this.service.deleteRole(role);
        return ResponseEntity.ok().build();
    }
}
