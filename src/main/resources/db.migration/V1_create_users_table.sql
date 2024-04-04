CREATE TABLE IF NOT EXISTS users
(
    id       uuid not null,
    avatar
    varchar(255),
    email    varchar(255) unique,
    name     varchar(255),
    password varchar(255),
    role     varchar(255),
    primary key (id)
    );