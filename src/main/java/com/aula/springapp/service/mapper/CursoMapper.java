package com.aula.springapp.service.mapper;

import com.aula.springapp.domain.Curso;
import com.aula.springapp.service.dto.CursoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Curso} and its DTO {@link CursoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CursoMapper extends EntityMapper<CursoDTO, Curso> {}
