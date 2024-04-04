package net.dunice.mk.mkhachemizov_testserver.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.dunice.mk.mkhachemizov_testserver.entity.NewsEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    @EntityGraph(attributePaths = {"tags", "userEntity"})
    @Query("SELECT n FROM NewsEntity n JOIN FETCH n.userEntity WHERE n.id = :id AND n.userEntity.id = :userId")
    Optional<NewsEntity> putNews(long id, UUID userId);


    @Query("SELECT DISTINCT n FROM NewsEntity n " +
            "LEFT JOIN FETCH n.userEntity u " +
            "LEFT JOIN FETCH n.tags t " +
            "WHERE (:author IS NULL OR u.name LIKE %:author%) " +
            "AND (:keywords IS NULL OR (n.title LIKE %:keywords% OR n.description LIKE %:keywords% OR t.title LIKE %:keywords%)) " +
            "AND (:tags IS NULL OR t.title IN :tags)")
    Page<NewsEntity> findNews(String author, String keywords, List<String> tags, Pageable pageable);

    @Query("SELECT DISTINCT n FROM NewsEntity n " +
            "INNER JOIN FETCH n.userEntity u " +
            "INNER JOIN FETCH n.tags t " +
            "WHERE :userid IS NULL OR u.id = :userid")
    Page<NewsEntity> findUserNews(UUID userid, Pageable pageable);
}

