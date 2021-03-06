package ru.job4j.domain;

import ru.job4j.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class
    })
    private int id;

    @NotBlank(message = "Username must be not empty", groups = {
            Operation.OnCreate.class, Operation.OnUpdate.class
    })
    private String username;

    @NotBlank(message = "Password must be not empty", groups = {
            Operation.OnCreate.class, Operation.OnUpdate.class
    })
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @NotNull(message = "Role must be not null", groups = {
            Operation.OnCreate.class, Operation.OnUpdate.class
    })
    private Role role;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "persons_rooms",
            joinColumns = {@JoinColumn(name = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "room_id")}
    )
    private final List<Room> rooms = new ArrayList<>();

    public static Person of(String username, String password, Role role) {
        Person person = new Person();
        person.username = username;
        person.password = password;
        person.role = role;
        return person;
    }

    public Room addRoom(Room room) {
        this.rooms.add(room);
        return room;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
