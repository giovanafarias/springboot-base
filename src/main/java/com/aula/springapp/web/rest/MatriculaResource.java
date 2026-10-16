package com.aula.springapp.web.rest;

import com.aula.springapp.repository.MatriculaRepository;
import com.aula.springapp.service.MatriculaService;
import com.aula.springapp.service.dto.MatriculaDTO;
import com.aula.springapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.aula.springapp.domain.Matricula}.
 */
@RestController
@RequestMapping("/api/matriculas")
public class MatriculaResource {

    private static final Logger LOG = LoggerFactory.getLogger(MatriculaResource.class);

    private static final String ENTITY_NAME = "matricula";

    @Value("${jhipster.clientApp.name:springapp}")
    private String applicationName;

    private final MatriculaService matriculaService;

    private final MatriculaRepository matriculaRepository;

    public MatriculaResource(MatriculaService matriculaService, MatriculaRepository matriculaRepository) {
        this.matriculaService = matriculaService;
        this.matriculaRepository = matriculaRepository;
    }

    /**
     * {@code POST  /matriculas} : Create a new matricula.
     *
     * @param matriculaDTO the matriculaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new matriculaDTO, or with status {@code 400 (Bad Request)} if the matricula already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MatriculaDTO> createMatricula(@Valid @RequestBody MatriculaDTO matriculaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Matricula : {}", matriculaDTO);
        if (matriculaDTO.getId() != null) {
            throw new BadRequestAlertException("A new matricula cannot already have an ID", ENTITY_NAME, "idexists");
        }
        matriculaDTO = matriculaService.save(matriculaDTO);
        return ResponseEntity.created(new URI("/api/matriculas/" + matriculaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, matriculaDTO.getId()))
            .body(matriculaDTO);
    }

    /**
     * {@code PUT  /matriculas/:id} : Updates an existing matricula.
     *
     * @param id the id of the matriculaDTO to save.
     * @param matriculaDTO the matriculaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matriculaDTO,
     * or with status {@code 400 (Bad Request)} if the matriculaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the matriculaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MatriculaDTO> updateMatricula(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody MatriculaDTO matriculaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Matricula : {}, {}", id, matriculaDTO);
        if (matriculaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matriculaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matriculaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        matriculaDTO = matriculaService.update(matriculaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matriculaDTO.getId()))
            .body(matriculaDTO);
    }

    /**
     * {@code PATCH  /matriculas/:id} : Partial updates given fields of an existing matricula, field will ignore if it is null
     *
     * @param id the id of the matriculaDTO to save.
     * @param matriculaDTO the matriculaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matriculaDTO,
     * or with status {@code 400 (Bad Request)} if the matriculaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the matriculaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the matriculaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MatriculaDTO> partialUpdateMatricula(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody MatriculaDTO matriculaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Matricula : {}, {}", id, matriculaDTO);
        if (matriculaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matriculaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matriculaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MatriculaDTO> result = matriculaService.partialUpdate(matriculaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matriculaDTO.getId())
        );
    }

    /**
     * {@code GET  /matriculas} : get all the Matriculas.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Matriculas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MatriculaDTO>> getAllMatriculas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Matriculas");
        Page<MatriculaDTO> page;
        if (eagerload) {
            page = matriculaService.findAllWithEagerRelationships(pageable);
        } else {
            page = matriculaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /matriculas/:id} : get the "id" matricula.
     *
     * @param id the id of the matriculaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the matriculaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MatriculaDTO> getMatricula(@PathVariable("id") String id) {
        LOG.debug("REST request to get Matricula : {}", id);
        Optional<MatriculaDTO> matriculaDTO = matriculaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(matriculaDTO);
    }

    /**
     * {@code DELETE  /matriculas/:id} : delete the "id" matricula.
     *
     * @param id the id of the matriculaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatricula(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Matricula : {}", id);
        matriculaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id))
            .build();
    }
}
