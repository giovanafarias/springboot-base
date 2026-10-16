package com.aula.springapp.service.mapper;

import com.aula.springapp.domain.Aluno;
import com.aula.springapp.domain.Curso;
import com.aula.springapp.service.dto.AlunoDTO;
import com.aula.springapp.service.dto.CursoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Aluno} and its DTO {@link AlunoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlunoMapper extends EntityMapper<AlunoDTO, Aluno> {
    @Mapping(target = "curso", source = "curso", qualifiedByName = "cursoNome")
    AlunoDTO toDto(Aluno s);

    @Named("cursoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    CursoDTO toDtoCursoNome(Curso curso);
}
