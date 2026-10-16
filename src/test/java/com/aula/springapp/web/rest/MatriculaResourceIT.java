package com.aula.springapp.web.rest;

import static com.aula.springapp.domain.MatriculaAsserts.*;
import static com.aula.springapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aula.springapp.IntegrationTest;
import com.aula.springapp.domain.Matricula;
import com.aula.springapp.repository.MatriculaRepository;
import com.aula.springapp.service.MatriculaService;
import com.aula.springapp.service.dto.MatriculaDTO;
import com.aula.springapp.service.mapper.MatriculaMapper;
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
 * Integration tests for the {@link MatriculaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MatriculaResourceIT {

    private static final Integer DEFAULT_ANO_LETIVO = 1;
    private static final Integer UPDATED_ANO_LETIVO = 2;

    private static final String ENTITY_API_URL = "/api/matriculas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Mock
    private MatriculaRepository matriculaRepositoryMock;

    @Autowired
    private MatriculaMapper matriculaMapper;

    @Mock
    private MatriculaService matriculaServiceMock;

    @Autowired
    private MockMvc restMatriculaMockMvc;

    private Matricula matricula;

    private Matricula insertedMatricula;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matricula createEntity() {
        return new Matricula().anoLetivo(DEFAULT_ANO_LETIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matricula createUpdatedEntity() {
        return new Matricula().anoLetivo(UPDATED_ANO_LETIVO);
    }

    @BeforeEach
    void initTest() {
        matricula = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMatricula != null) {
            matriculaRepository.delete(insertedMatricula);
            insertedMatricula = null;
        }
    }

    @Test
    void createMatricula() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Matricula
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);
        var returnedMatriculaDTO = om.readValue(
            restMatriculaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matriculaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MatriculaDTO.class
        );

        // Validate the Matricula in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMatricula = matriculaMapper.toEntity(returnedMatriculaDTO);
        assertMatriculaUpdatableFieldsEquals(returnedMatricula, getPersistedMatricula(returnedMatricula));

        insertedMatricula = returnedMatricula;
    }

    @Test
    void createMatriculaWithExistingId() throws Exception {
        // Create the Matricula with an existing ID
        matricula.setId("existing_id");
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatriculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matriculaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkAnoLetivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        matricula.setAnoLetivo(null);

        // Create the Matricula, which fails.
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        restMatriculaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matriculaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllMatriculas() throws Exception {
        // Initialize the database
        insertedMatricula = matriculaRepository.save(matricula);

        // Get all the matriculaList
        restMatriculaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matricula.getId())))
            .andExpect(jsonPath("$.[*].anoLetivo").value(hasItem(DEFAULT_ANO_LETIVO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMatriculasWithEagerRelationshipsIsEnabled() throws Exception {
        when(matriculaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMatriculaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(matriculaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMatriculasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(matriculaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMatriculaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(matriculaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getMatricula() throws Exception {
        // Initialize the database
        insertedMatricula = matriculaRepository.save(matricula);

        // Get the matricula
        restMatriculaMockMvc
            .perform(get(ENTITY_API_URL_ID, matricula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matricula.getId()))
            .andExpect(jsonPath("$.anoLetivo").value(DEFAULT_ANO_LETIVO));
    }

    @Test
    void getNonExistingMatricula() throws Exception {
        // Get the matricula
        restMatriculaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingMatricula() throws Exception {
        // Initialize the database
        insertedMatricula = matriculaRepository.save(matricula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the matricula
        Matricula updatedMatricula = matriculaRepository.findById(matricula.getId()).orElseThrow();
        updatedMatricula.anoLetivo(UPDATED_ANO_LETIVO);
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(updatedMatricula);

        restMatriculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matriculaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(matriculaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMatriculaToMatchAllProperties(updatedMatricula);
    }

    @Test
    void putNonExistingMatricula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matricula.setId(UUID.randomUUID().toString());

        // Create the Matricula
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matriculaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(matriculaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMatricula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matricula.setId(UUID.randomUUID().toString());

        // Create the Matricula
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(matriculaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMatricula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matricula.setId(UUID.randomUUID().toString());

        // Create the Matricula
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(matriculaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMatriculaWithPatch() throws Exception {
        // Initialize the database
        insertedMatricula = matriculaRepository.save(matricula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the matricula using partial update
        Matricula partialUpdatedMatricula = new Matricula();
        partialUpdatedMatricula.setId(matricula.getId());

        partialUpdatedMatricula.anoLetivo(UPDATED_ANO_LETIVO);

        restMatriculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatricula.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMatricula))
            )
            .andExpect(status().isOk());

        // Validate the Matricula in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMatriculaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMatricula, matricula),
            getPersistedMatricula(matricula)
        );
    }

    @Test
    void fullUpdateMatriculaWithPatch() throws Exception {
        // Initialize the database
        insertedMatricula = matriculaRepository.save(matricula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the matricula using partial update
        Matricula partialUpdatedMatricula = new Matricula();
        partialUpdatedMatricula.setId(matricula.getId());

        partialUpdatedMatricula.anoLetivo(UPDATED_ANO_LETIVO);

        restMatriculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatricula.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMatricula))
            )
            .andExpect(status().isOk());

        // Validate the Matricula in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMatriculaUpdatableFieldsEquals(partialUpdatedMatricula, getPersistedMatricula(partialUpdatedMatricula));
    }

    @Test
    void patchNonExistingMatricula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matricula.setId(UUID.randomUUID().toString());

        // Create the Matricula
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, matriculaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(matriculaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMatricula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matricula.setId(UUID.randomUUID().toString());

        // Create the Matricula
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(matriculaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMatricula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        matricula.setId(UUID.randomUUID().toString());

        // Create the Matricula
        MatriculaDTO matriculaDTO = matriculaMapper.toDto(matricula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriculaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(matriculaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matricula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMatricula() throws Exception {
        // Initialize the database
        insertedMatricula = matriculaRepository.save(matricula);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the matricula
        restMatriculaMockMvc
            .perform(delete(ENTITY_API_URL_ID, matricula.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return matriculaRepository.count();
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

    protected Matricula getPersistedMatricula(Matricula matricula) {
        return matriculaRepository.findById(matricula.getId()).orElseThrow();
    }

    protected void assertPersistedMatriculaToMatchAllProperties(Matricula expectedMatricula) {
        assertMatriculaAllPropertiesEquals(expectedMatricula, getPersistedMatricula(expectedMatricula));
    }

    protected void assertPersistedMatriculaToMatchUpdatableProperties(Matricula expectedMatricula) {
        assertMatriculaAllUpdatablePropertiesEquals(expectedMatricula, getPersistedMatricula(expectedMatricula));
    }
}
