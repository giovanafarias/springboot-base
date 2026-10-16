package com.aula.springapp.service.mapper;

import com.aula.springapp.domain.Curso;
import com.aula.springapp.domain.Disciplina;
import com.aula.springapp.service.dto.CursoDTO;
import com.aula.springapp.service.dto.DisciplinaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Disciplina} and its DTO {@link DisciplinaDTO}.
 */
@Mapper(componentModel = "spring")
public interface DisciplinaMapper extends EntityMapper<DisciplinaDTO, Disciplina> {
    @Mapping(target = "curso", source = "curso", qualifiedByName = "cursoNome")
    DisciplinaDTO toDto(Disciplina s);

    @Named("cursoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    CursoDTO toDtoCursoNome(Curso curso);
}
