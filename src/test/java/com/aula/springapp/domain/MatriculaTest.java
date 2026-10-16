package com.aula.springapp.domain;

import static com.aula.springapp.domain.AlunoTestSamples.*;
import static com.aula.springapp.domain.MatriculaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aula.springapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatriculaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Matricula.class);
        Matricula matricula1 = getMatriculaSample1();
        Matricula matricula2 = new Matricula();
        assertThat(matricula1).isNotEqualTo(matricula2);

        matricula2.setId(matricula1.getId());
        assertThat(matricula1).isEqualTo(matricula2);

        matricula2 = getMatriculaSample2();
        assertThat(matricula1).isNotEqualTo(matricula2);
    }

    @Test
    void alunoTest() {
        Matricula matricula = getMatriculaRandomSampleGenerator();
        Aluno alunoBack = getAlunoRandomSampleGenerator();

        matricula.setAluno(alunoBack);
        assertThat(matricula.getAluno()).isEqualTo(alunoBack);

        matricula.aluno(null);
        assertThat(matricula.getAluno()).isNull();
    }
}
