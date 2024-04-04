CREATE TABLE IF NOT EXISTS logs
(
    id         bigserial not null,
    created_at timestamp(6),
    method     varchar(255),
    status     int,
    user_id    varchar(255),
    primary key (id)
);