package com.aula.springapp.service.mapper;

import static com.aula.springapp.domain.DisciplinaAsserts.*;
import static com.aula.springapp.domain.DisciplinaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DisciplinaMapperTest {

    private DisciplinaMapper disciplinaMapper;

    @BeforeEach
    void setUp() {
        disciplinaMapper = new DisciplinaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDisciplinaSample1();
        var actual = disciplinaMapper.toEntity(disciplinaMapper.toDto(expected));
        assertDisciplinaAllPropertiesEquals(expected, actual);
    }
}
