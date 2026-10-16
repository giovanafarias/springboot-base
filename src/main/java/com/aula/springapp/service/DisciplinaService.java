package com.aula.springapp.service;

import com.aula.springapp.service.dto.DisciplinaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.aula.springapp.domain.Disciplina}.
 */
public interface DisciplinaService {
    /**
     * Save a disciplina.
     *
     * @param disciplinaDTO the entity to save.
     * @return the persisted entity.
     */
    DisciplinaDTO save(DisciplinaDTO disciplinaDTO);

    /**
     * Updates a disciplina.
     *
     * @param disciplinaDTO the entity to update.
     * @return the persisted entity.
     */
    DisciplinaDTO update(DisciplinaDTO disciplinaDTO);

    /**
     * Partially updates a disciplina.
     *
     * @param disciplinaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DisciplinaDTO> partialUpdate(DisciplinaDTO disciplinaDTO);

    /**
     * Get all the disciplinas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DisciplinaDTO> findAll(Pageable pageable);

    /**
     * Get all the disciplinas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DisciplinaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" disciplina.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DisciplinaDTO> findOne(String id);

    /**
     * Delete the "id" disciplina.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
