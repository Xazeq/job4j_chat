package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
        if (id <= 0) {
            throw new NullPointerException("Id should be more than 0");
        }
        Role role = service.findRoleById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Role not found. Please, check id."
        ));
        return new ResponseEntity<>(
                role,
                HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity<Role> create(@RequestBody Role role) {
        if (role.getName() == null) {
            throw new NullPointerException("Role name can`t be empty");
        }
        return new ResponseEntity<>(
                this.service.saveRole(role),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Role role) {
        if (role.getName() == null) {
            throw new NullPointerException("Role name can`t be empty");
        }
        this.service.saveRole(role);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (id <= 0) {
            throw new NullPointerException("Id should be more than 0");
        }
        Role role = new Role();
        role.setId(id);
        this.service.deleteRole(role);
        return ResponseEntity.ok().build();
    }
}
