package com.aula.springapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.aula.springapp.domain.Matricula} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MatriculaDTO implements Serializable {

    private String id;

    @NotNull
    private Integer anoLetivo;

    private AlunoDTO aluno;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(Integer anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public AlunoDTO getAluno() {
        return aluno;
    }

    public void setAluno(AlunoDTO aluno) {
        this.aluno = aluno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatriculaDTO)) {
            return false;
        }

        MatriculaDTO matriculaDTO = (MatriculaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, matriculaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatriculaDTO{" +
            "id='" + getId() + "'" +
            ", anoLetivo=" + getAnoLetivo() +
            ", aluno=" + getAluno() +
            "}";
    }
}
