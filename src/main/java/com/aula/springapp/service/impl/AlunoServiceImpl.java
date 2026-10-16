package com.aula.springapp.service.impl;

import com.aula.springapp.domain.Aluno;
import com.aula.springapp.repository.AlunoRepository;
import com.aula.springapp.service.AlunoService;
import com.aula.springapp.service.dto.AlunoDTO;
import com.aula.springapp.service.mapper.AlunoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.aula.springapp.domain.Aluno}.
 */
@Service
public class AlunoServiceImpl implements AlunoService {

    private static final Logger LOG = LoggerFactory.getLogger(AlunoServiceImpl.class);

    private final AlunoRepository alunoRepository;

    private final AlunoMapper alunoMapper;

    public AlunoServiceImpl(AlunoRepository alunoRepository, AlunoMapper alunoMapper) {
        this.alunoRepository = alunoRepository;
        this.alunoMapper = alunoMapper;
    }

    @Override
    public AlunoDTO save(AlunoDTO alunoDTO) {
        LOG.debug("Request to save Aluno : {}", alunoDTO);
        Aluno aluno = alunoMapper.toEntity(alunoDTO);
        aluno = alunoRepository.save(aluno);
        return alunoMapper.toDto(aluno);
    }

    @Override
    public AlunoDTO update(AlunoDTO alunoDTO) {
        LOG.debug("Request to update Aluno : {}", alunoDTO);
        Aluno aluno = alunoMapper.toEntity(alunoDTO);
        aluno = alunoRepository.save(aluno);
        return alunoMapper.toDto(aluno);
    }

    @Override
    public Optional<AlunoDTO> partialUpdate(AlunoDTO alunoDTO) {
        LOG.debug("Request to partially update Aluno : {}", alunoDTO);

        return alunoRepository
            .findById(alunoDTO.getId())
            .map(existingAluno -> {
                alunoMapper.partialUpdate(existingAluno, alunoDTO);

                return existingAluno;
            })
            .map(alunoRepository::save)
            .map(alunoMapper::toDto);
    }

    @Override
    public Page<AlunoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Alunos");
        return alunoRepository.findAll(pageable).map(alunoMapper::toDto);
    }

    public Page<AlunoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return alunoRepository.findAllWithEagerRelationships(pageable).map(alunoMapper::toDto);
    }

    @Override
    public Optional<AlunoDTO> findOne(String id) {
        LOG.debug("Request to get Aluno : {}", id);
        return alunoRepository.findOneWithEagerRelationships(id).map(alunoMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Aluno : {}", id);
        alunoRepository.deleteById(id);
    }
}
