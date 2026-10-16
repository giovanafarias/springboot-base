package com.aula.springapp.domain;

import static com.aula.springapp.domain.AlunoTestSamples.*;
import static com.aula.springapp.domain.CursoTestSamples.*;
import static com.aula.springapp.domain.DisciplinaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aula.springapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CursoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Curso.class);
        Curso curso1 = getCursoSample1();
        Curso curso2 = new Curso();
        assertThat(curso1).isNotEqualTo(curso2);

        curso2.setId(curso1.getId());
        assertThat(curso1).isEqualTo(curso2);

        curso2 = getCursoSample2();
        assertThat(curso1).isNotEqualTo(curso2);
    }

    @Test
    void disciplinaTest() {
        Curso curso = getCursoRandomSampleGenerator();
        Disciplina disciplinaBack = getDisciplinaRandomSampleGenerator();

        curso.addDisciplina(disciplinaBack);
        assertThat(curso.getDisciplinas()).containsOnly(disciplinaBack);
        assertThat(disciplinaBack.getCurso()).isEqualTo(curso);

        curso.removeDisciplina(disciplinaBack);
        assertThat(curso.getDisciplinas()).doesNotContain(disciplinaBack);
        assertThat(disciplinaBack.getCurso()).isNull();

        curso.disciplinas(new HashSet<>(Set.of(disciplinaBack)));
        assertThat(curso.getDisciplinas()).containsOnly(disciplinaBack);
        assertThat(disciplinaBack.getCurso()).isEqualTo(curso);

        curso.setDisciplinas(new HashSet<>());
        assertThat(curso.getDisciplinas()).doesNotContain(disciplinaBack);
        assertThat(disciplinaBack.getCurso()).isNull();
    }

    @Test
    void alunoTest() {
        Curso curso = getCursoRandomSampleGenerator();
        Aluno alunoBack = getAlunoRandomSampleGenerator();

        curso.addAluno(alunoBack);
        assertThat(curso.getAlunos()).containsOnly(alunoBack);
        assertThat(alunoBack.getCurso()).isEqualTo(curso);

        curso.removeAluno(alunoBack);
        assertThat(curso.getAlunos()).doesNotContain(alunoBack);
        assertThat(alunoBack.getCurso()).isNull();

        curso.alunos(new HashSet<>(Set.of(alunoBack)));
        assertThat(curso.getAlunos()).containsOnly(alunoBack);
        assertThat(alunoBack.getCurso()).isEqualTo(curso);

        curso.setAlunos(new HashSet<>());
        assertThat(curso.getAlunos()).doesNotContain(alunoBack);
        assertThat(alunoBack.getCurso()).isNull();
    }
}
