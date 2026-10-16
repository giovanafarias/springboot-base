package com.aula.springapp.service;

import com.aula.springapp.service.dto.AlunoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.aula.springapp.domain.Aluno}.
 */
public interface AlunoService {
    /**
     * Save a aluno.
     *
     * @param alunoDTO the entity to save.
     * @return the persisted entity.
     */
    AlunoDTO save(AlunoDTO alunoDTO);

    /**
     * Updates a aluno.
     *
     * @param alunoDTO the entity to update.
     * @return the persisted entity.
     */
    AlunoDTO update(AlunoDTO alunoDTO);

    /**
     * Partially updates a aluno.
     *
     * @param alunoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlunoDTO> partialUpdate(AlunoDTO alunoDTO);

    /**
     * Get all the alunos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlunoDTO> findAll(Pageable pageable);

    /**
     * Get all the alunos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlunoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" aluno.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlunoDTO> findOne(String id);

    /**
     * Delete the "id" aluno.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
