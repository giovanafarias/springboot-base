package com.aula.springapp.web.rest;

import com.aula.springapp.repository.DisciplinaRepository;
import com.aula.springapp.service.DisciplinaService;
import com.aula.springapp.service.dto.DisciplinaDTO;
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
 * REST controller for managing {@link com.aula.springapp.domain.Disciplina}.
 */
@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaResource {

    private static final Logger LOG = LoggerFactory.getLogger(DisciplinaResource.class);

    private static final String ENTITY_NAME = "disciplina";

    @Value("${jhipster.clientApp.name:springapp}")
    private String applicationName;

    private final DisciplinaService disciplinaService;

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaResource(DisciplinaService disciplinaService, DisciplinaRepository disciplinaRepository) {
        this.disciplinaService = disciplinaService;
        this.disciplinaRepository = disciplinaRepository;
    }

    /**
     * {@code POST  /disciplinas} : Create a new disciplina.
     *
     * @param disciplinaDTO the disciplinaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new disciplinaDTO, or with status {@code 400 (Bad Request)} if the disciplina already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DisciplinaDTO> createDisciplina(@Valid @RequestBody DisciplinaDTO disciplinaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Disciplina : {}", disciplinaDTO);
        if (disciplinaDTO.getId() != null) {
            throw new BadRequestAlertException("A new disciplina cannot already have an ID", ENTITY_NAME, "idexists");
        }
        disciplinaDTO = disciplinaService.save(disciplinaDTO);
        return ResponseEntity.created(new URI("/api/disciplinas/" + disciplinaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, disciplinaDTO.getId()))
            .body(disciplinaDTO);
    }

    /**
     * {@code PUT  /disciplinas/:id} : Updates an existing disciplina.
     *
     * @param id the id of the disciplinaDTO to save.
     * @param disciplinaDTO the disciplinaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disciplinaDTO,
     * or with status {@code 400 (Bad Request)} if the disciplinaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the disciplinaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> updateDisciplina(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody DisciplinaDTO disciplinaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Disciplina : {}, {}", id, disciplinaDTO);
        if (disciplinaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disciplinaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disciplinaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        disciplinaDTO = disciplinaService.update(disciplinaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, disciplinaDTO.getId()))
            .body(disciplinaDTO);
    }

    /**
     * {@code PATCH  /disciplinas/:id} : Partial updates given fields of an existing disciplina, field will ignore if it is null
     *
     * @param id the id of the disciplinaDTO to save.
     * @param disciplinaDTO the disciplinaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disciplinaDTO,
     * or with status {@code 400 (Bad Request)} if the disciplinaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the disciplinaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the disciplinaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DisciplinaDTO> partialUpdateDisciplina(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody DisciplinaDTO disciplinaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Disciplina : {}, {}", id, disciplinaDTO);
        if (disciplinaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disciplinaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disciplinaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DisciplinaDTO> result = disciplinaService.partialUpdate(disciplinaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, disciplinaDTO.getId())
        );
    }

    /**
     * {@code GET  /disciplinas} : get all the Disciplinas.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Disciplinas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DisciplinaDTO>> getAllDisciplinas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Disciplinas");
        Page<DisciplinaDTO> page;
        if (eagerload) {
            page = disciplinaService.findAllWithEagerRelationships(pageable);
        } else {
            page = disciplinaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /disciplinas/:id} : get the "id" disciplina.
     *
     * @param id the id of the disciplinaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the disciplinaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> getDisciplina(@PathVariable("id") String id) {
        LOG.debug("REST request to get Disciplina : {}", id);
        Optional<DisciplinaDTO> disciplinaDTO = disciplinaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(disciplinaDTO);
    }

    /**
     * {@code DELETE  /disciplinas/:id} : delete the "id" disciplina.
     *
     * @param id the id of the disciplinaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisciplina(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Disciplina : {}", id);
        disciplinaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id))
            .build();
    }
}
