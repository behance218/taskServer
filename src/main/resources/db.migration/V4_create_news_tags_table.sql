CREATE TABLE IF NOT EXISTS news_tags
(
    news_id bigint not null,
    tags_id bigint not null,
    primary key (news_id, tags_id)
);
ALTER TABLE IF EXISTS news_tags
    ADD CONSTRAINT news_tags_tags_fk
        FOREIGN KEY (tags_id) REFERENCES tags;
ALTER TABLE IF EXISTS news_tags
    ADD CONSTRAINT news_tags_news_fk
        FOREIGN KEY (news_id) REFERENCES news;