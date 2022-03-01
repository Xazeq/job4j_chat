create table if not exists roles(
    id serial primary key,
    name varchar(100)
);

create table if not exists persons(
    id serial primary key,
    username varchar(100),
    password varchar(100),
    role_id int references roles(id)
);

create table if not exists rooms(
    id serial primary key,
    name varchar(200),
    created timestamp
);

create table if not exists messages(
    id serial primary key,
    text text,
    created timestamp
);

create table if not exists persons_rooms(
    person_id int references persons(id),
    room_id int references rooms(id)
);

create table if not exists rooms_messages(
    room_id int references rooms(id),
    message_id int references messages(id)
);

insert into roles (name) values ('user'), ('admin');

insert into persons(username, password, role_id)
values ('user1', '123', 1), ('root', '123', 2), ('user2', '123', 1);

insert into rooms(name, created)
values ('room1', '2022-03-01 10:00:00'),
       ('room2', '2022-03-01 10:01:00');

insert into messages(text, created)
values ('message1', '2022-03-01 10:00:00'),
       ('message2', '2022-03-01 10:01:00'),
       ('message3', '2022-03-01 10:02:00'),
       ('message4', '2022-03-01 10:03:00');

insert into persons_rooms (person_id, room_id)
values (1, 1), (2, 2), (3, 2);

insert into rooms_messages (room_id, message_id)
values (1, 1), (1, 3), (2, 2), (2, 4);