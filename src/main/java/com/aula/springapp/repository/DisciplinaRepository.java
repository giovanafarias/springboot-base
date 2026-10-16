package com.aula.springapp.repository;

import com.aula.springapp.domain.Disciplina;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Disciplina entity.
 */
@Repository
public interface DisciplinaRepository extends MongoRepository<Disciplina, String> {
    @Query("{}")
    Page<Disciplina> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Disciplina> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Disciplina> findOneWithEagerRelationships(String id);
}
