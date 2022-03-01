package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.domain.Role;
import ru.job4j.domain.Room;
import ru.job4j.repository.MessageRepository;
import ru.job4j.repository.PersonRepository;
import ru.job4j.repository.RoleRepository;
import ru.job4j.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;

    public ChatService(RoleRepository roleRepository, PersonRepository personRepository,
                       RoomRepository roomRepository, MessageRepository messageRepository) {
        this.roleRepository = roleRepository;
        this.personRepository = personRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
    }

    public List<Person> findAllPersons() {
        return (List<Person>) personRepository.findAll();
    }

    public List<Role> findAllRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    public List<Room> findAllRooms() {
        return (List<Room>) roomRepository.findAll();
    }

    public List<Message> findAllMessages() {
        return (List<Message>) messageRepository.findAll();
    }

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public Optional<Person> findPersonById(int id) {
        return personRepository.findById(id);
    }

    public Optional<Role> findRoleById(int id) {
        return roleRepository.findById(id);
    }

    public Optional<Room> findRoomById(int id) {
        return roomRepository.findById(id);
    }

    public Optional<Message> findMessageById(int id) {
        return messageRepository.findById(id);
    }

    public void deletePerson(Person person) {
        personRepository.delete(person);
    }

    public void deleteRole(Role role) {
        roleRepository.delete(role);
    }

    public void deleteRoom(Room room) {
        roomRepository.delete(room);
    }

    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }
}
