package com.aula.springapp.service.impl;

import com.aula.springapp.domain.Curso;
import com.aula.springapp.repository.CursoRepository;
import com.aula.springapp.service.CursoService;
import com.aula.springapp.service.dto.CursoDTO;
import com.aula.springapp.service.mapper.CursoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.aula.springapp.domain.Curso}.
 */
@Service
public class CursoServiceImpl implements CursoService {

    private static final Logger LOG = LoggerFactory.getLogger(CursoServiceImpl.class);

    private final CursoRepository cursoRepository;

    private final CursoMapper cursoMapper;

    public CursoServiceImpl(CursoRepository cursoRepository, CursoMapper cursoMapper) {
        this.cursoRepository = cursoRepository;
        this.cursoMapper = cursoMapper;
    }

    @Override
    public CursoDTO save(CursoDTO cursoDTO) {
        LOG.debug("Request to save Curso : {}", cursoDTO);
        Curso curso = cursoMapper.toEntity(cursoDTO);
        curso = cursoRepository.save(curso);
        return cursoMapper.toDto(curso);
    }

    @Override
    public CursoDTO update(CursoDTO cursoDTO) {
        LOG.debug("Request to update Curso : {}", cursoDTO);
        Curso curso = cursoMapper.toEntity(cursoDTO);
        curso = cursoRepository.save(curso);
        return cursoMapper.toDto(curso);
    }

    @Override
    public Optional<CursoDTO> partialUpdate(CursoDTO cursoDTO) {
        LOG.debug("Request to partially update Curso : {}", cursoDTO);

        return cursoRepository
            .findById(cursoDTO.getId())
            .map(existingCurso -> {
                cursoMapper.partialUpdate(existingCurso, cursoDTO);

                return existingCurso;
            })
            .map(cursoRepository::save)
            .map(cursoMapper::toDto);
    }

    @Override
    public Page<CursoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Cursos");
        return cursoRepository.findAll(pageable).map(cursoMapper::toDto);
    }

    @Override
    public Optional<CursoDTO> findOne(String id) {
        LOG.debug("Request to get Curso : {}", id);
        return cursoRepository.findById(id).map(cursoMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Curso : {}", id);
        cursoRepository.deleteById(id);
    }
}
