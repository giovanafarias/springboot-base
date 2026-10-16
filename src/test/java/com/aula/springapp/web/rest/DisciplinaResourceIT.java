package com.aula.springapp.web.rest;

import static com.aula.springapp.domain.DisciplinaAsserts.*;
import static com.aula.springapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aula.springapp.IntegrationTest;
import com.aula.springapp.domain.Disciplina;
import com.aula.springapp.repository.DisciplinaRepository;
import com.aula.springapp.service.DisciplinaService;
import com.aula.springapp.service.dto.DisciplinaDTO;
import com.aula.springapp.service.mapper.DisciplinaMapper;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link DisciplinaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DisciplinaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CARGA_HORARIA = 1;
    private static final Integer UPDATED_CARGA_HORARIA = 2;

    private static final String ENTITY_API_URL = "/api/disciplinas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Mock
    private DisciplinaRepository disciplinaRepositoryMock;

    @Autowired
    private DisciplinaMapper disciplinaMapper;

    @Mock
    private DisciplinaService disciplinaServiceMock;

    @Autowired
    private MockMvc restDisciplinaMockMvc;

    private Disciplina disciplina;

    private Disciplina insertedDisciplina;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disciplina createEntity() {
        return new Disciplina().nome(DEFAULT_NOME).cargaHoraria(DEFAULT_CARGA_HORARIA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disciplina createUpdatedEntity() {
        return new Disciplina().nome(UPDATED_NOME).cargaHoraria(UPDATED_CARGA_HORARIA);
    }

    @BeforeEach
    void initTest() {
        disciplina = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDisciplina != null) {
            disciplinaRepository.delete(insertedDisciplina);
            insertedDisciplina = null;
        }
    }

    @Test
    void createDisciplina() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Disciplina
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);
        var returnedDisciplinaDTO = om.readValue(
            restDisciplinaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disciplinaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DisciplinaDTO.class
        );

        // Validate the Disciplina in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDisciplina = disciplinaMapper.toEntity(returnedDisciplinaDTO);
        assertDisciplinaUpdatableFieldsEquals(returnedDisciplina, getPersistedDisciplina(returnedDisciplina));

        insertedDisciplina = returnedDisciplina;
    }

    @Test
    void createDisciplinaWithExistingId() throws Exception {
        // Create the Disciplina with an existing ID
        disciplina.setId("existing_id");
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisciplinaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disciplinaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        disciplina.setNome(null);

        // Create the Disciplina, which fails.
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        restDisciplinaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disciplinaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCargaHorariaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        disciplina.setCargaHoraria(null);

        // Create the Disciplina, which fails.
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        restDisciplinaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disciplinaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllDisciplinas() throws Exception {
        // Initialize the database
        insertedDisciplina = disciplinaRepository.save(disciplina);

        // Get all the disciplinaList
        restDisciplinaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disciplina.getId())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cargaHoraria").value(hasItem(DEFAULT_CARGA_HORARIA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDisciplinasWithEagerRelationshipsIsEnabled() throws Exception {
        when(disciplinaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDisciplinaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(disciplinaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDisciplinasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(disciplinaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDisciplinaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(disciplinaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getDisciplina() throws Exception {
        // Initialize the database
        insertedDisciplina = disciplinaRepository.save(disciplina);

        // Get the disciplina
        restDisciplinaMockMvc
            .perform(get(ENTITY_API_URL_ID, disciplina.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disciplina.getId()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cargaHoraria").value(DEFAULT_CARGA_HORARIA));
    }

    @Test
    void getNonExistingDisciplina() throws Exception {
        // Get the disciplina
        restDisciplinaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingDisciplina() throws Exception {
        // Initialize the database
        insertedDisciplina = disciplinaRepository.save(disciplina);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disciplina
        Disciplina updatedDisciplina = disciplinaRepository.findById(disciplina.getId()).orElseThrow();
        updatedDisciplina.nome(UPDATED_NOME).cargaHoraria(UPDATED_CARGA_HORARIA);
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(updatedDisciplina);

        restDisciplinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disciplinaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disciplinaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDisciplinaToMatchAllProperties(updatedDisciplina);
    }

    @Test
    void putNonExistingDisciplina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disciplina.setId(UUID.randomUUID().toString());

        // Create the Disciplina
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisciplinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disciplinaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disciplinaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDisciplina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disciplina.setId(UUID.randomUUID().toString());

        // Create the Disciplina
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disciplinaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDisciplina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disciplina.setId(UUID.randomUUID().toString());

        // Create the Disciplina
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplinaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disciplinaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDisciplinaWithPatch() throws Exception {
        // Initialize the database
        insertedDisciplina = disciplinaRepository.save(disciplina);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disciplina using partial update
        Disciplina partialUpdatedDisciplina = new Disciplina();
        partialUpdatedDisciplina.setId(disciplina.getId());

        partialUpdatedDisciplina.cargaHoraria(UPDATED_CARGA_HORARIA);

        restDisciplinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisciplina.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisciplina))
            )
            .andExpect(status().isOk());

        // Validate the Disciplina in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDisciplinaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDisciplina, disciplina),
            getPersistedDisciplina(disciplina)
        );
    }

    @Test
    void fullUpdateDisciplinaWithPatch() throws Exception {
        // Initialize the database
        insertedDisciplina = disciplinaRepository.save(disciplina);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disciplina using partial update
        Disciplina partialUpdatedDisciplina = new Disciplina();
        partialUpdatedDisciplina.setId(disciplina.getId());

        partialUpdatedDisciplina.nome(UPDATED_NOME).cargaHoraria(UPDATED_CARGA_HORARIA);

        restDisciplinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisciplina.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisciplina))
            )
            .andExpect(status().isOk());

        // Validate the Disciplina in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDisciplinaUpdatableFieldsEquals(partialUpdatedDisciplina, getPersistedDisciplina(partialUpdatedDisciplina));
    }

    @Test
    void patchNonExistingDisciplina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disciplina.setId(UUID.randomUUID().toString());

        // Create the Disciplina
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisciplinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, disciplinaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disciplinaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDisciplina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disciplina.setId(UUID.randomUUID().toString());

        // Create the Disciplina
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disciplinaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDisciplina() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disciplina.setId(UUID.randomUUID().toString());

        // Create the Disciplina
        DisciplinaDTO disciplinaDTO = disciplinaMapper.toDto(disciplina);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplinaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(disciplinaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disciplina in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDisciplina() throws Exception {
        // Initialize the database
        insertedDisciplina = disciplinaRepository.save(disciplina);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the disciplina
        restDisciplinaMockMvc
            .perform(delete(ENTITY_API_URL_ID, disciplina.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return disciplinaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Disciplina getPersistedDisciplina(Disciplina disciplina) {
        return disciplinaRepository.findById(disciplina.getId()).orElseThrow();
    }

    protected void assertPersistedDisciplinaToMatchAllProperties(Disciplina expectedDisciplina) {
        assertDisciplinaAllPropertiesEquals(expectedDisciplina, getPersistedDisciplina(expectedDisciplina));
    }

    protected void assertPersistedDisciplinaToMatchUpdatableProperties(Disciplina expectedDisciplina) {
        assertDisciplinaAllUpdatablePropertiesEquals(expectedDisciplina, getPersistedDisciplina(expectedDisciplina));
    }
}
