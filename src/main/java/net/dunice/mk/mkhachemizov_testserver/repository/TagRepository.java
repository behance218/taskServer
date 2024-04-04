package net.dunice.mk.mkhachemizov_testserver.repository;

import java.util.Optional;

import net.dunice.mk.mkhachemizov_testserver.entity.TagEntity;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TagRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByTitle(String title);
}
