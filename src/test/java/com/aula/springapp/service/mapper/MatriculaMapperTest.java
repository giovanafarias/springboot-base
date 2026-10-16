package com.aula.springapp.service.mapper;

import static com.aula.springapp.domain.MatriculaAsserts.*;
import static com.aula.springapp.domain.MatriculaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatriculaMapperTest {

    private MatriculaMapper matriculaMapper;

    @BeforeEach
    void setUp() {
        matriculaMapper = new MatriculaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMatriculaSample1();
        var actual = matriculaMapper.toEntity(matriculaMapper.toDto(expected));
        assertMatriculaAllPropertiesEquals(expected, actual);
    }
}
