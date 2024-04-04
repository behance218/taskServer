CREATE TABLE IF NOT EXISTS news
(
    id             bigserial not null,
    description    varchar(255),
    image          varchar(255),
    title          varchar(255),
    user_entity_id uuid,
    primary key (id)
);
alter table if exists news
    add constraint news_user_fk
        foreign key (user_entity_id) references users;