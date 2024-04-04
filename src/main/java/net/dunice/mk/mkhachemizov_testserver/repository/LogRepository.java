package net.dunice.mk.mkhachemizov_testserver.repository;

import net.dunice.mk.mkhachemizov_testserver.entity.LogEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
}
