package com.hmvsoluciones.saas.web.rest;

import com.hmvsoluciones.saas.repository.SuscripcionRepository;
import com.hmvsoluciones.saas.service.SuscripcionQueryService;
import com.hmvsoluciones.saas.service.SuscripcionService;
import com.hmvsoluciones.saas.service.criteria.SuscripcionCriteria;
import com.hmvsoluciones.saas.service.dto.SuscripcionDTO;
import com.hmvsoluciones.saas.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.hmvsoluciones.saas.domain.Suscripcion}.
 */
@RestController
@RequestMapping("/api/suscripcions")
public class SuscripcionResource {

    private static final Logger LOG = LoggerFactory.getLogger(SuscripcionResource.class);

    private static final String ENTITY_NAME = "suscripcion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuscripcionService suscripcionService;

    private final SuscripcionRepository suscripcionRepository;

    private final SuscripcionQueryService suscripcionQueryService;

    public SuscripcionResource(
        SuscripcionService suscripcionService,
        SuscripcionRepository suscripcionRepository,
        SuscripcionQueryService suscripcionQueryService
    ) {
        this.suscripcionService = suscripcionService;
        this.suscripcionRepository = suscripcionRepository;
        this.suscripcionQueryService = suscripcionQueryService;
    }

    /**
     * {@code POST  /suscripcions} : Create a new suscripcion.
     *
     * @param suscripcionDTO the suscripcionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new suscripcionDTO, or with status {@code 400 (Bad Request)} if the suscripcion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SuscripcionDTO> createSuscripcion(@Valid @RequestBody SuscripcionDTO suscripcionDTO) throws URISyntaxException {
        LOG.debug("REST request to save Suscripcion : {}", suscripcionDTO);
        if (suscripcionDTO.getId() != null) {
            throw new BadRequestAlertException("A new suscripcion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        suscripcionDTO = suscripcionService.save(suscripcionDTO);
        return ResponseEntity.created(new URI("/api/suscripcions/" + suscripcionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, suscripcionDTO.getId().toString()))
            .body(suscripcionDTO);
    }

    /**
     * {@code PUT  /suscripcions/:id} : Updates an existing suscripcion.
     *
     * @param id the id of the suscripcionDTO to save.
     * @param suscripcionDTO the suscripcionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suscripcionDTO,
     * or with status {@code 400 (Bad Request)} if the suscripcionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the suscripcionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SuscripcionDTO> updateSuscripcion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SuscripcionDTO suscripcionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Suscripcion : {}, {}", id, suscripcionDTO);
        if (suscripcionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, suscripcionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!suscripcionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        suscripcionDTO = suscripcionService.update(suscripcionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, suscripcionDTO.getId().toString()))
            .body(suscripcionDTO);
    }

    /**
     * {@code PATCH  /suscripcions/:id} : Partial updates given fields of an existing suscripcion, field will ignore if it is null
     *
     * @param id the id of the suscripcionDTO to save.
     * @param suscripcionDTO the suscripcionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suscripcionDTO,
     * or with status {@code 400 (Bad Request)} if the suscripcionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the suscripcionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the suscripcionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SuscripcionDTO> partialUpdateSuscripcion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SuscripcionDTO suscripcionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Suscripcion partially : {}, {}", id, suscripcionDTO);
        if (suscripcionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, suscripcionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!suscripcionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SuscripcionDTO> result = suscripcionService.partialUpdate(suscripcionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, suscripcionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /suscripcions} : get all the suscripcions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suscripcions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SuscripcionDTO>> getAllSuscripcions(
        SuscripcionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Suscripcions by criteria: {}", criteria);

        Page<SuscripcionDTO> page = suscripcionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /suscripcions/count} : count all the suscripcions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSuscripcions(SuscripcionCriteria criteria) {
        LOG.debug("REST request to count Suscripcions by criteria: {}", criteria);
        return ResponseEntity.ok().body(suscripcionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /suscripcions/:id} : get the "id" suscripcion.
     *
     * @param id the id of the suscripcionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the suscripcionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuscripcionDTO> getSuscripcion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Suscripcion : {}", id);
        Optional<SuscripcionDTO> suscripcionDTO = suscripcionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(suscripcionDTO);
    }

    /**
     * {@code DELETE  /suscripcions/:id} : delete the "id" suscripcion.
     *
     * @param id the id of the suscripcionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuscripcion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Suscripcion : {}", id);
        suscripcionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
