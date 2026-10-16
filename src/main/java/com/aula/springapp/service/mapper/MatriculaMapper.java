package com.aula.springapp.service.mapper;

import com.aula.springapp.domain.Aluno;
import com.aula.springapp.domain.Matricula;
import com.aula.springapp.service.dto.AlunoDTO;
import com.aula.springapp.service.dto.MatriculaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Matricula} and its DTO {@link MatriculaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MatriculaMapper extends EntityMapper<MatriculaDTO, Matricula> {
    @Mapping(target = "aluno", source = "aluno", qualifiedByName = "alunoNome")
    MatriculaDTO toDto(Matricula s);

    @Named("alunoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    AlunoDTO toDtoAlunoNome(Aluno aluno);
}
