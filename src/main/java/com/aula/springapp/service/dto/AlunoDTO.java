package com.aula.springapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.aula.springapp.domain.Aluno} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlunoDTO implements Serializable {

    private String id;

    @NotNull
    private String nome;

    private CursoDTO curso;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CursoDTO getCurso() {
        return curso;
    }

    public void setCurso(CursoDTO curso) {
        this.curso = curso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlunoDTO)) {
            return false;
        }

        AlunoDTO alunoDTO = (AlunoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alunoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlunoDTO{" +
            "id='" + getId() + "'" +
            ", nome='" + getNome() + "'" +
            ", curso=" + getCurso() +
            "}";
    }
}
