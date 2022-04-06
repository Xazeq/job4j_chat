package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Role;
import ru.job4j.handlers.Operation;
import ru.job4j.service.ChatService;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/roles")
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
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Role> create(@Valid @RequestBody Role role) {
        if (role.getName() == null) {
            throw new NullPointerException("Role name can`t be empty");
        }
        return new ResponseEntity<>(
                this.service.saveRole(role),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Role role) {
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

    @PatchMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Role> partialUpdateRole(@Valid @RequestBody Role role)
            throws InvocationTargetException, IllegalAccessException {
        return new ResponseEntity<>(
                this.service.partialUpdateRole(role),
                HttpStatus.OK
        );
    }
}
