package com.aula.springapp.service.impl;

import com.aula.springapp.domain.Disciplina;
import com.aula.springapp.repository.DisciplinaRepository;
import com.aula.springapp.service.DisciplinaService;
import com.aula.springapp.service.dto.DisciplinaDTO;
import com.aula.springapp.service.mapper.DisciplinaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.aula.springapp.domain.Disciplina}.
 */
@Service
public class DisciplinaServiceImpl implements DisciplinaService {

    private static final Logger LOG = LoggerFactory.getLogger(DisciplinaServiceImpl.class);

    private final DisciplinaRepository disciplinaRepository;

    private final DisciplinaMapper disciplinaMapper;

    public DisciplinaServiceImpl(DisciplinaRepository disciplinaRepository, DisciplinaMapper disciplinaMapper) {
        this.disciplinaRepository = disciplinaRepository;
        this.disciplinaMapper = disciplinaMapper;
    }

    @Override
    public DisciplinaDTO save(DisciplinaDTO disciplinaDTO) {
        LOG.debug("Request to save Disciplina : {}", disciplinaDTO);
        Disciplina disciplina = disciplinaMapper.toEntity(disciplinaDTO);
        disciplina = disciplinaRepository.save(disciplina);
        return disciplinaMapper.toDto(disciplina);
    }

    @Override
    public DisciplinaDTO update(DisciplinaDTO disciplinaDTO) {
        LOG.debug("Request to update Disciplina : {}", disciplinaDTO);
        Disciplina disciplina = disciplinaMapper.toEntity(disciplinaDTO);
        disciplina = disciplinaRepository.save(disciplina);
        return disciplinaMapper.toDto(disciplina);
    }

    @Override
    public Optional<DisciplinaDTO> partialUpdate(DisciplinaDTO disciplinaDTO) {
        LOG.debug("Request to partially update Disciplina : {}", disciplinaDTO);

        return disciplinaRepository
            .findById(disciplinaDTO.getId())
            .map(existingDisciplina -> {
                disciplinaMapper.partialUpdate(existingDisciplina, disciplinaDTO);

                return existingDisciplina;
            })
            .map(disciplinaRepository::save)
            .map(disciplinaMapper::toDto);
    }

    @Override
    public Page<DisciplinaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Disciplinas");
        return disciplinaRepository.findAll(pageable).map(disciplinaMapper::toDto);
    }

    public Page<DisciplinaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return disciplinaRepository.findAllWithEagerRelationships(pageable).map(disciplinaMapper::toDto);
    }

    @Override
    public Optional<DisciplinaDTO> findOne(String id) {
        LOG.debug("Request to get Disciplina : {}", id);
        return disciplinaRepository.findOneWithEagerRelationships(id).map(disciplinaMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Disciplina : {}", id);
        disciplinaRepository.deleteById(id);
    }
}
