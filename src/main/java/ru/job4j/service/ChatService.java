package ru.job4j.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.domain.Role;
import ru.job4j.domain.Room;
import ru.job4j.repository.MessageRepository;
import ru.job4j.repository.PersonRepository;
import ru.job4j.repository.RoleRepository;
import ru.job4j.repository.RoomRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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

    public Person partialUpdatePerson(Person person)
            throws InvocationTargetException, IllegalAccessException {
        return this.partialUpdate(personRepository, person, person.getId());
    }

    public Role partialUpdateRole(Role role)
            throws InvocationTargetException, IllegalAccessException {
        return this.partialUpdate(roleRepository, role, role.getId());
    }

    public Room partialUpdateRoom(Room room)
            throws InvocationTargetException, IllegalAccessException {
        return this.partialUpdate(roomRepository, room, room.getId());
    }

    public Message partialUpdateMessage(Message message)
            throws InvocationTargetException, IllegalAccessException {
        return this.partialUpdate(messageRepository, message, message.getId());
    }

    private <T> T partialUpdate(CrudRepository<T, Integer> repository, T model, int id)
            throws InvocationTargetException, IllegalAccessException {
        var optionalCurrent = repository.findById(id);
        T current;
        if (optionalCurrent.isPresent()) {
            current = optionalCurrent.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Object not found. Please, check id.");
        }
        var methods = current.getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method : methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    continue;
                }
                var newValue = getMethod.invoke(model);
                if (newValue != null) {
                    setMethod.invoke(current, newValue);
                }
            }
        }
        return repository.save(current);
    }
}
