package com.aula.springapp.service.impl;

import com.aula.springapp.domain.Matricula;
import com.aula.springapp.repository.MatriculaRepository;
import com.aula.springapp.service.MatriculaService;
import com.aula.springapp.service.dto.MatriculaDTO;
import com.aula.springapp.service.mapper.MatriculaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.aula.springapp.domain.Matricula}.
 */
@Service
public class MatriculaServiceImpl implements MatriculaService {

    private static final Logger LOG = LoggerFactory.getLogger(MatriculaServiceImpl.class);

    private final MatriculaRepository matriculaRepository;

    private final MatriculaMapper matriculaMapper;

    public MatriculaServiceImpl(MatriculaRepository matriculaRepository, MatriculaMapper matriculaMapper) {
        this.matriculaRepository = matriculaRepository;
        this.matriculaMapper = matriculaMapper;
    }

    @Override
    public MatriculaDTO save(MatriculaDTO matriculaDTO) {
        LOG.debug("Request to save Matricula : {}", matriculaDTO);
        Matricula matricula = matriculaMapper.toEntity(matriculaDTO);
        matricula = matriculaRepository.save(matricula);
        return matriculaMapper.toDto(matricula);
    }

    @Override
    public MatriculaDTO update(MatriculaDTO matriculaDTO) {
        LOG.debug("Request to update Matricula : {}", matriculaDTO);
        Matricula matricula = matriculaMapper.toEntity(matriculaDTO);
        matricula = matriculaRepository.save(matricula);
        return matriculaMapper.toDto(matricula);
    }

    @Override
    public Optional<MatriculaDTO> partialUpdate(MatriculaDTO matriculaDTO) {
        LOG.debug("Request to partially update Matricula : {}", matriculaDTO);

        return matriculaRepository
            .findById(matriculaDTO.getId())
            .map(existingMatricula -> {
                matriculaMapper.partialUpdate(existingMatricula, matriculaDTO);

                return existingMatricula;
            })
            .map(matriculaRepository::save)
            .map(matriculaMapper::toDto);
    }

    @Override
    public Page<MatriculaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Matriculas");
        return matriculaRepository.findAll(pageable).map(matriculaMapper::toDto);
    }

    public Page<MatriculaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return matriculaRepository.findAllWithEagerRelationships(pageable).map(matriculaMapper::toDto);
    }

    @Override
    public Optional<MatriculaDTO> findOne(String id) {
        LOG.debug("Request to get Matricula : {}", id);
        return matriculaRepository.findOneWithEagerRelationships(id).map(matriculaMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Matricula : {}", id);
        matriculaRepository.deleteById(id);
    }
}
