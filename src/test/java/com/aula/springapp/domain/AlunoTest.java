package com.aula.springapp.domain;

import static com.aula.springapp.domain.AlunoTestSamples.*;
import static com.aula.springapp.domain.CursoTestSamples.*;
import static com.aula.springapp.domain.MatriculaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aula.springapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlunoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aluno.class);
        Aluno aluno1 = getAlunoSample1();
        Aluno aluno2 = new Aluno();
        assertThat(aluno1).isNotEqualTo(aluno2);

        aluno2.setId(aluno1.getId());
        assertThat(aluno1).isEqualTo(aluno2);

        aluno2 = getAlunoSample2();
        assertThat(aluno1).isNotEqualTo(aluno2);
    }

    @Test
    void matriculaTest() {
        Aluno aluno = getAlunoRandomSampleGenerator();
        Matricula matriculaBack = getMatriculaRandomSampleGenerator();

        aluno.addMatricula(matriculaBack);
        assertThat(aluno.getMatriculas()).containsOnly(matriculaBack);
        assertThat(matriculaBack.getAluno()).isEqualTo(aluno);

        aluno.removeMatricula(matriculaBack);
        assertThat(aluno.getMatriculas()).doesNotContain(matriculaBack);
        assertThat(matriculaBack.getAluno()).isNull();

        aluno.matriculas(new HashSet<>(Set.of(matriculaBack)));
        assertThat(aluno.getMatriculas()).containsOnly(matriculaBack);
        assertThat(matriculaBack.getAluno()).isEqualTo(aluno);

        aluno.setMatriculas(new HashSet<>());
        assertThat(aluno.getMatriculas()).doesNotContain(matriculaBack);
        assertThat(matriculaBack.getAluno()).isNull();
    }

    @Test
    void cursoTest() {
        Aluno aluno = getAlunoRandomSampleGenerator();
        Curso cursoBack = getCursoRandomSampleGenerator();

        aluno.setCurso(cursoBack);
        assertThat(aluno.getCurso()).isEqualTo(cursoBack);

        aluno.curso(null);
        assertThat(aluno.getCurso()).isNull();
    }
}
