CREATE DATABASE if not exists aupo;
use aupo;

drop table if exists book;
drop table if exists book_genre;
drop table if exists book_in_order;
drop table if exists genre;
drop table if exists user;
drop table if exists user_order;

create table book
(
    id int auto_increment
        primary key,
    author mediumtext null,
    title mediumtext null,
    price double null
);

create table book_genre
(
    id int auto_increment
        primary key,
    book_id int not null,
    genre_id int not null
);

create table book_in_order
(
    id int auto_increment
        primary key,
    book_id int not null,
    order_id int not null
);

create table genre
(
    id int auto_increment
        primary key,
    name mediumtext null
);

create table user
(
    id int auto_increment
        primary key,
    login mediumtext not null,
    password mediumtext not null,
    role mediumtext not null
);

create table user_order
(
    id int auto_increment
        primary key,
    order_date timestamp null,
    user_id int null
);

insert into genre (name) values ('Classic');
insert into genre (name) values ('Fantastic');
insert into genre (name) values ('Foreign literature');

INSERT INTO user (login, password, role) VALUES ('root', '$2a$10$sIGDpQaPWP6rknCWLlWgHedOnkhP/Y7R.LudH/D16OebNEJYNh2bC', 'ROLE_ADMIN');

insert into book (author, title, price) values ('Pushkin', 'Evgeniy Onegin', 11.22);
insert into book (author, title, price) values ('Pushkin', 'Kapitanskaya dochka', 1.33);
insert into book (author, title, price) values ('Tolstoi', 'Voina i mir', 99.22);
insert into book (author, title, price) values ('Belyaev', 'Zvezda KEC', 7.43);
insert into book (author, title, price) values ('Dyuma', 'Tri mushketera', 17.43);

insert into book_genre(book_id, genre_id) values (1,1);
insert into book_genre(book_id, genre_id) values (2,1);
insert into book_genre(book_id, genre_id) values (3,1);
insert into book_genre(book_id, genre_id) values (4,1);
insert into book_genre(book_id, genre_id) values (4,2);
insert into book_genre(book_id, genre_id) values (5,1);
insert into book_genre(book_id, genre_id) values (5,3);
