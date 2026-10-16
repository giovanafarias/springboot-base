package com.aula.springapp.web.rest;

import static com.aula.springapp.domain.CursoAsserts.*;
import static com.aula.springapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aula.springapp.IntegrationTest;
import com.aula.springapp.domain.Curso;
import com.aula.springapp.repository.CursoRepository;
import com.aula.springapp.service.dto.CursoDTO;
import com.aula.springapp.service.mapper.CursoMapper;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link CursoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CursoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURACAO = 1;
    private static final Integer UPDATED_DURACAO = 2;

    private static final String ENTITY_API_URL = "/api/cursos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CursoMapper cursoMapper;

    @Autowired
    private MockMvc restCursoMockMvc;

    private Curso curso;

    private Curso insertedCurso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Curso createEntity() {
        return new Curso().nome(DEFAULT_NOME).duracao(DEFAULT_DURACAO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Curso createUpdatedEntity() {
        return new Curso().nome(UPDATED_NOME).duracao(UPDATED_DURACAO);
    }

    @BeforeEach
    void initTest() {
        curso = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCurso != null) {
            cursoRepository.delete(insertedCurso);
            insertedCurso = null;
        }
    }

    @Test
    void createCurso() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Curso
        CursoDTO cursoDTO = cursoMapper.toDto(curso);
        var returnedCursoDTO = om.readValue(
            restCursoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cursoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CursoDTO.class
        );

        // Validate the Curso in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCurso = cursoMapper.toEntity(returnedCursoDTO);
        assertCursoUpdatableFieldsEquals(returnedCurso, getPersistedCurso(returnedCurso));

        insertedCurso = returnedCurso;
    }

    @Test
    void createCursoWithExistingId() throws Exception {
        // Create the Curso with an existing ID
        curso.setId("existing_id");
        CursoDTO cursoDTO = cursoMapper.toDto(curso);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCursoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cursoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        curso.setNome(null);

        // Create the Curso, which fails.
        CursoDTO cursoDTO = cursoMapper.toDto(curso);

        restCursoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cursoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDuracaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        curso.setDuracao(null);

        // Create the Curso, which fails.
        CursoDTO cursoDTO = cursoMapper.toDto(curso);

        restCursoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cursoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllCursos() throws Exception {
        // Initialize the database
        insertedCurso = cursoRepository.save(curso);

        // Get all the cursoList
        restCursoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(curso.getId())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].duracao").value(hasItem(DEFAULT_DURACAO)));
    }

    @Test
    void getCurso() throws Exception {
        // Initialize the database
        insertedCurso = cursoRepository.save(curso);

        // Get the curso
        restCursoMockMvc
            .perform(get(ENTITY_API_URL_ID, curso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(curso.getId()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.duracao").value(DEFAULT_DURACAO));
    }

    @Test
    void getNonExistingCurso() throws Exception {
        // Get the curso
        restCursoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingCurso() throws Exception {
        // Initialize the database
        insertedCurso = cursoRepository.save(curso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the curso
        Curso updatedCurso = cursoRepository.findById(curso.getId()).orElseThrow();
        updatedCurso.nome(UPDATED_NOME).duracao(UPDATED_DURACAO);
        CursoDTO cursoDTO = cursoMapper.toDto(updatedCurso);

        restCursoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cursoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cursoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCursoToMatchAllProperties(updatedCurso);
    }

    @Test
    void putNonExistingCurso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curso.setId(UUID.randomUUID().toString());

        // Create the Curso
        CursoDTO cursoDTO = cursoMapper.toDto(curso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cursoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cursoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCurso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curso.setId(UUID.randomUUID().toString());

        // Create the Curso
        CursoDTO cursoDTO = cursoMapper.toDto(curso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cursoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCurso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curso.setId(UUID.randomUUID().toString());

        // Create the Curso
        CursoDTO cursoDTO = cursoMapper.toDto(curso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cursoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCursoWithPatch() throws Exception {
        // Initialize the database
        insertedCurso = cursoRepository.save(curso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the curso using partial update
        Curso partialUpdatedCurso = new Curso();
        partialUpdatedCurso.setId(curso.getId());

        restCursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCurso))
            )
            .andExpect(status().isOk());

        // Validate the Curso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCursoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCurso, curso), getPersistedCurso(curso));
    }

    @Test
    void fullUpdateCursoWithPatch() throws Exception {
        // Initialize the database
        insertedCurso = cursoRepository.save(curso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the curso using partial update
        Curso partialUpdatedCurso = new Curso();
        partialUpdatedCurso.setId(curso.getId());

        partialUpdatedCurso.nome(UPDATED_NOME).duracao(UPDATED_DURACAO);

        restCursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCurso))
            )
            .andExpect(status().isOk());

        // Validate the Curso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCursoUpdatableFieldsEquals(partialUpdatedCurso, getPersistedCurso(partialUpdatedCurso));
    }

    @Test
    void patchNonExistingCurso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curso.setId(UUID.randomUUID().toString());

        // Create the Curso
        CursoDTO cursoDTO = cursoMapper.toDto(curso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cursoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cursoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCurso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curso.setId(UUID.randomUUID().toString());

        // Create the Curso
        CursoDTO cursoDTO = cursoMapper.toDto(curso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cursoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCurso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        curso.setId(UUID.randomUUID().toString());

        // Create the Curso
        CursoDTO cursoDTO = cursoMapper.toDto(curso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCursoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cursoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Curso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCurso() throws Exception {
        // Initialize the database
        insertedCurso = cursoRepository.save(curso);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the curso
        restCursoMockMvc
            .perform(delete(ENTITY_API_URL_ID, curso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cursoRepository.count();
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

    protected Curso getPersistedCurso(Curso curso) {
        return cursoRepository.findById(curso.getId()).orElseThrow();
    }

    protected void assertPersistedCursoToMatchAllProperties(Curso expectedCurso) {
        assertCursoAllPropertiesEquals(expectedCurso, getPersistedCurso(expectedCurso));
    }

    protected void assertPersistedCursoToMatchUpdatableProperties(Curso expectedCurso) {
        assertCursoAllUpdatablePropertiesEquals(expectedCurso, getPersistedCurso(expectedCurso));
    }
}
