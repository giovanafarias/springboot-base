package com.aula.springapp.repository;

import com.aula.springapp.domain.Matricula;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Matricula entity.
 */
@Repository
public interface MatriculaRepository extends MongoRepository<Matricula, String> {
    @Query("{}")
    Page<Matricula> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Matricula> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Matricula> findOneWithEagerRelationships(String id);
}
