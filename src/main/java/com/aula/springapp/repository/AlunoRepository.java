package com.aula.springapp.repository;

import com.aula.springapp.domain.Aluno;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Aluno entity.
 */
@Repository
public interface AlunoRepository extends MongoRepository<Aluno, String> {
    @Query("{}")
    Page<Aluno> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Aluno> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Aluno> findOneWithEagerRelationships(String id);
}
