package com.aula.springapp.repository;

import com.aula.springapp.domain.Curso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Curso entity.
 */
@Repository
public interface CursoRepository extends MongoRepository<Curso, String> {}
