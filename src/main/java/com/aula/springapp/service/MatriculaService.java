package com.aula.springapp.service;

import com.aula.springapp.service.dto.MatriculaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.aula.springapp.domain.Matricula}.
 */
public interface MatriculaService {
    /**
     * Save a matricula.
     *
     * @param matriculaDTO the entity to save.
     * @return the persisted entity.
     */
    MatriculaDTO save(MatriculaDTO matriculaDTO);

    /**
     * Updates a matricula.
     *
     * @param matriculaDTO the entity to update.
     * @return the persisted entity.
     */
    MatriculaDTO update(MatriculaDTO matriculaDTO);

    /**
     * Partially updates a matricula.
     *
     * @param matriculaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MatriculaDTO> partialUpdate(MatriculaDTO matriculaDTO);

    /**
     * Get all the matriculas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MatriculaDTO> findAll(Pageable pageable);

    /**
     * Get all the matriculas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MatriculaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" matricula.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MatriculaDTO> findOne(String id);

    /**
     * Delete the "id" matricula.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
