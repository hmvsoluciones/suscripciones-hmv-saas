package com.hmvsoluciones.saas.web.rest;

import com.hmvsoluciones.saas.repository.PlanRepository;
import com.hmvsoluciones.saas.service.PlanQueryService;
import com.hmvsoluciones.saas.service.PlanService;
import com.hmvsoluciones.saas.service.criteria.PlanCriteria;
import com.hmvsoluciones.saas.service.dto.PlanDTO;
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
 * REST controller for managing {@link com.hmvsoluciones.saas.domain.Plan}.
 */
@RestController
@RequestMapping("/api/plans")
public class PlanResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlanResource.class);

    private static final String ENTITY_NAME = "plan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanService planService;

    private final PlanRepository planRepository;

    private final PlanQueryService planQueryService;

    public PlanResource(PlanService planService, PlanRepository planRepository, PlanQueryService planQueryService) {
        this.planService = planService;
        this.planRepository = planRepository;
        this.planQueryService = planQueryService;
    }

    /**
     * {@code POST  /plans} : Create a new plan.
     *
     * @param planDTO the planDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planDTO, or with status {@code 400 (Bad Request)} if the plan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlanDTO> createPlan(@Valid @RequestBody PlanDTO planDTO) throws URISyntaxException {
        LOG.debug("REST request to save Plan : {}", planDTO);
        if (planDTO.getId() != null) {
            throw new BadRequestAlertException("A new plan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        planDTO = planService.save(planDTO);
        return ResponseEntity.created(new URI("/api/plans/" + planDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, planDTO.getId().toString()))
            .body(planDTO);
    }

    /**
     * {@code PUT  /plans/:id} : Updates an existing plan.
     *
     * @param id the id of the planDTO to save.
     * @param planDTO the planDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planDTO,
     * or with status {@code 400 (Bad Request)} if the planDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlanDTO> updatePlan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlanDTO planDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Plan : {}, {}", id, planDTO);
        if (planDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        planDTO = planService.update(planDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, planDTO.getId().toString()))
            .body(planDTO);
    }

    /**
     * {@code PATCH  /plans/:id} : Partial updates given fields of an existing plan, field will ignore if it is null
     *
     * @param id the id of the planDTO to save.
     * @param planDTO the planDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planDTO,
     * or with status {@code 400 (Bad Request)} if the planDTO is not valid,
     * or with status {@code 404 (Not Found)} if the planDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the planDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlanDTO> partialUpdatePlan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlanDTO planDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Plan partially : {}, {}", id, planDTO);
        if (planDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlanDTO> result = planService.partialUpdate(planDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, planDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /plans} : get all the plans.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plans in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlanDTO>> getAllPlans(
        PlanCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Plans by criteria: {}", criteria);

        Page<PlanDTO> page = planQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plans/count} : count all the plans.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPlans(PlanCriteria criteria) {
        LOG.debug("REST request to count Plans by criteria: {}", criteria);
        return ResponseEntity.ok().body(planQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /plans/:id} : get the "id" plan.
     *
     * @param id the id of the planDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlanDTO> getPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Plan : {}", id);
        Optional<PlanDTO> planDTO = planService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planDTO);
    }

    /**
     * {@code DELETE  /plans/:id} : delete the "id" plan.
     *
     * @param id the id of the planDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Plan : {}", id);
        planService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
