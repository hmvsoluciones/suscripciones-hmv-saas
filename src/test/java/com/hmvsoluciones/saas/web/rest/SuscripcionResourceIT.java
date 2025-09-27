package com.hmvsoluciones.saas.web.rest;

import static com.hmvsoluciones.saas.domain.SuscripcionAsserts.*;
import static com.hmvsoluciones.saas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmvsoluciones.saas.IntegrationTest;
import com.hmvsoluciones.saas.domain.Cliente;
import com.hmvsoluciones.saas.domain.Plan;
import com.hmvsoluciones.saas.domain.Suscripcion;
import com.hmvsoluciones.saas.domain.enumeration.EstadoSuscripcion;
import com.hmvsoluciones.saas.repository.SuscripcionRepository;
import com.hmvsoluciones.saas.service.dto.SuscripcionDTO;
import com.hmvsoluciones.saas.service.mapper.SuscripcionMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SuscripcionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SuscripcionResourceIT {

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_INICIO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_FIN = LocalDate.ofEpochDay(-1L);

    private static final EstadoSuscripcion DEFAULT_ESTADO = EstadoSuscripcion.ACTIVA;
    private static final EstadoSuscripcion UPDATED_ESTADO = EstadoSuscripcion.CANCELADA;

    private static final String ENTITY_API_URL = "/api/suscripcions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SuscripcionRepository suscripcionRepository;

    @Autowired
    private SuscripcionMapper suscripcionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSuscripcionMockMvc;

    private Suscripcion suscripcion;

    private Suscripcion insertedSuscripcion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Suscripcion createEntity() {
        return new Suscripcion().fechaInicio(DEFAULT_FECHA_INICIO).fechaFin(DEFAULT_FECHA_FIN).estado(DEFAULT_ESTADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Suscripcion createUpdatedEntity() {
        return new Suscripcion().fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).estado(UPDATED_ESTADO);
    }

    @BeforeEach
    void initTest() {
        suscripcion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSuscripcion != null) {
            suscripcionRepository.delete(insertedSuscripcion);
            insertedSuscripcion = null;
        }
    }

    @Test
    @Transactional
    void createSuscripcion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Suscripcion
        SuscripcionDTO suscripcionDTO = suscripcionMapper.toDto(suscripcion);
        var returnedSuscripcionDTO = om.readValue(
            restSuscripcionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suscripcionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SuscripcionDTO.class
        );

        // Validate the Suscripcion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSuscripcion = suscripcionMapper.toEntity(returnedSuscripcionDTO);
        assertSuscripcionUpdatableFieldsEquals(returnedSuscripcion, getPersistedSuscripcion(returnedSuscripcion));

        insertedSuscripcion = returnedSuscripcion;
    }

    @Test
    @Transactional
    void createSuscripcionWithExistingId() throws Exception {
        // Create the Suscripcion with an existing ID
        suscripcion.setId(1L);
        SuscripcionDTO suscripcionDTO = suscripcionMapper.toDto(suscripcion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuscripcionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suscripcionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Suscripcion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaInicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        suscripcion.setFechaInicio(null);

        // Create the Suscripcion, which fails.
        SuscripcionDTO suscripcionDTO = suscripcionMapper.toDto(suscripcion);

        restSuscripcionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suscripcionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaFinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        suscripcion.setFechaFin(null);

        // Create the Suscripcion, which fails.
        SuscripcionDTO suscripcionDTO = suscripcionMapper.toDto(suscripcion);

        restSuscripcionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suscripcionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        suscripcion.setEstado(null);

        // Create the Suscripcion, which fails.
        SuscripcionDTO suscripcionDTO = suscripcionMapper.toDto(suscripcion);

        restSuscripcionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suscripcionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSuscripcions() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList
        restSuscripcionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suscripcion.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    void getSuscripcion() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get the suscripcion
        restSuscripcionMockMvc
            .perform(get(ENTITY_API_URL_ID, suscripcion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(suscripcion.getId().intValue()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getSuscripcionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        Long id = suscripcion.getId();

        defaultSuscripcionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSuscripcionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSuscripcionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSuscripcionsByFechaInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where fechaInicio equals to
        defaultSuscripcionFiltering("fechaInicio.equals=" + DEFAULT_FECHA_INICIO, "fechaInicio.equals=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllSuscripcionsByFechaInicioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where fechaInicio in
        defaultSuscripcionFiltering(
            "fechaInicio.in=" + DEFAULT_FECHA_INICIO + "," + UPDATED_FECHA_INICIO,
            "fechaInicio.in=" + UPDATED_FECHA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllSuscripcionsByFechaInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where fechaInicio is not null
        defaultSuscripcionFiltering("fechaInicio.specified=true", "fechaInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllSuscripcionsByFechaInicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where fechaInicio is greater than or equal to
        defaultSuscripcionFiltering(
            "fechaInicio.greaterThanOrEqual=" + DEFAULT_FECHA_INICIO,
            "fechaInicio.greaterThanOrEqual=" + UPDATED_FECHA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllSuscripcionsByFechaInicioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where fechaInicio is less than or equal to
        defaultSuscripcionFiltering(
            "fechaInicio.lessThanOrEqual=" + DEFAULT_FECHA_INICIO,
            "fechaInicio.lessThanOrEqual=" + SMALLER_FECHA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllSuscripcionsByFechaInicioIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where fechaInicio is less than
        defaultSuscripcionFiltering("fechaInicio.lessThan=" + UPDATED_FECHA_INICIO, "fechaInicio.lessThan=" + DEFAULT_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllSuscripcionsByFechaInicioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where fechaInicio is greater than
        defaultSuscripcionFiltering("fechaInicio.greaterThan=" + SMALLER_FECHA_INICIO, "fechaInicio.greaterThan=" + DEFAULT_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllSuscripcionsByFechaFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where fechaFin equals to
        defaultSuscripcionFiltering("fechaFin.equals=" + DEFAULT_FECHA_FIN, "fechaFin.equals=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllSuscripcionsByFechaFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where fechaFin in
        defaultSuscripcionFiltering("fechaFin.in=" + DEFAULT_FECHA_FIN + "," + UPDATED_FECHA_FIN, "fechaFin.in=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllSuscripcionsByFechaFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where fechaFin is not null
        defaultSuscripcionFiltering("fechaFin.specified=true", "fechaFin.specified=false");
    }

    @Test
    @Transactional
    void getAllSuscripcionsByFechaFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where fechaFin is greater than or equal to
        defaultSuscripcionFiltering("fechaFin.greaterThanOrEqual=" + DEFAULT_FECHA_FIN, "fechaFin.greaterThanOrEqual=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllSuscripcionsByFechaFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where fechaFin is less than or equal to
        defaultSuscripcionFiltering("fechaFin.lessThanOrEqual=" + DEFAULT_FECHA_FIN, "fechaFin.lessThanOrEqual=" + SMALLER_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllSuscripcionsByFechaFinIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where fechaFin is less than
        defaultSuscripcionFiltering("fechaFin.lessThan=" + UPDATED_FECHA_FIN, "fechaFin.lessThan=" + DEFAULT_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllSuscripcionsByFechaFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where fechaFin is greater than
        defaultSuscripcionFiltering("fechaFin.greaterThan=" + SMALLER_FECHA_FIN, "fechaFin.greaterThan=" + DEFAULT_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllSuscripcionsByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where estado equals to
        defaultSuscripcionFiltering("estado.equals=" + DEFAULT_ESTADO, "estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllSuscripcionsByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where estado in
        defaultSuscripcionFiltering("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO, "estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllSuscripcionsByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        // Get all the suscripcionList where estado is not null
        defaultSuscripcionFiltering("estado.specified=true", "estado.specified=false");
    }

    @Test
    @Transactional
    void getAllSuscripcionsByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            suscripcionRepository.saveAndFlush(suscripcion);
            cliente = ClienteResourceIT.createEntity();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        suscripcion.setCliente(cliente);
        suscripcionRepository.saveAndFlush(suscripcion);
        Long clienteId = cliente.getId();
        // Get all the suscripcionList where cliente equals to clienteId
        defaultSuscripcionShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the suscripcionList where cliente equals to (clienteId + 1)
        defaultSuscripcionShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    @Test
    @Transactional
    void getAllSuscripcionsByPlanIsEqualToSomething() throws Exception {
        Plan plan;
        if (TestUtil.findAll(em, Plan.class).isEmpty()) {
            suscripcionRepository.saveAndFlush(suscripcion);
            plan = PlanResourceIT.createEntity();
        } else {
            plan = TestUtil.findAll(em, Plan.class).get(0);
        }
        em.persist(plan);
        em.flush();
        suscripcion.setPlan(plan);
        suscripcionRepository.saveAndFlush(suscripcion);
        Long planId = plan.getId();
        // Get all the suscripcionList where plan equals to planId
        defaultSuscripcionShouldBeFound("planId.equals=" + planId);

        // Get all the suscripcionList where plan equals to (planId + 1)
        defaultSuscripcionShouldNotBeFound("planId.equals=" + (planId + 1));
    }

    private void defaultSuscripcionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSuscripcionShouldBeFound(shouldBeFound);
        defaultSuscripcionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSuscripcionShouldBeFound(String filter) throws Exception {
        restSuscripcionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suscripcion.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));

        // Check, that the count call also returns 1
        restSuscripcionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSuscripcionShouldNotBeFound(String filter) throws Exception {
        restSuscripcionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSuscripcionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSuscripcion() throws Exception {
        // Get the suscripcion
        restSuscripcionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSuscripcion() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the suscripcion
        Suscripcion updatedSuscripcion = suscripcionRepository.findById(suscripcion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSuscripcion are not directly saved in db
        em.detach(updatedSuscripcion);
        updatedSuscripcion.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).estado(UPDATED_ESTADO);
        SuscripcionDTO suscripcionDTO = suscripcionMapper.toDto(updatedSuscripcion);

        restSuscripcionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, suscripcionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(suscripcionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Suscripcion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSuscripcionToMatchAllProperties(updatedSuscripcion);
    }

    @Test
    @Transactional
    void putNonExistingSuscripcion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        suscripcion.setId(longCount.incrementAndGet());

        // Create the Suscripcion
        SuscripcionDTO suscripcionDTO = suscripcionMapper.toDto(suscripcion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuscripcionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, suscripcionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(suscripcionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Suscripcion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSuscripcion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        suscripcion.setId(longCount.incrementAndGet());

        // Create the Suscripcion
        SuscripcionDTO suscripcionDTO = suscripcionMapper.toDto(suscripcion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuscripcionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(suscripcionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Suscripcion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSuscripcion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        suscripcion.setId(longCount.incrementAndGet());

        // Create the Suscripcion
        SuscripcionDTO suscripcionDTO = suscripcionMapper.toDto(suscripcion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuscripcionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suscripcionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Suscripcion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSuscripcionWithPatch() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the suscripcion using partial update
        Suscripcion partialUpdatedSuscripcion = new Suscripcion();
        partialUpdatedSuscripcion.setId(suscripcion.getId());

        restSuscripcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuscripcion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSuscripcion))
            )
            .andExpect(status().isOk());

        // Validate the Suscripcion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSuscripcionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSuscripcion, suscripcion),
            getPersistedSuscripcion(suscripcion)
        );
    }

    @Test
    @Transactional
    void fullUpdateSuscripcionWithPatch() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the suscripcion using partial update
        Suscripcion partialUpdatedSuscripcion = new Suscripcion();
        partialUpdatedSuscripcion.setId(suscripcion.getId());

        partialUpdatedSuscripcion.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).estado(UPDATED_ESTADO);

        restSuscripcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuscripcion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSuscripcion))
            )
            .andExpect(status().isOk());

        // Validate the Suscripcion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSuscripcionUpdatableFieldsEquals(partialUpdatedSuscripcion, getPersistedSuscripcion(partialUpdatedSuscripcion));
    }

    @Test
    @Transactional
    void patchNonExistingSuscripcion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        suscripcion.setId(longCount.incrementAndGet());

        // Create the Suscripcion
        SuscripcionDTO suscripcionDTO = suscripcionMapper.toDto(suscripcion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuscripcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, suscripcionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(suscripcionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Suscripcion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSuscripcion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        suscripcion.setId(longCount.incrementAndGet());

        // Create the Suscripcion
        SuscripcionDTO suscripcionDTO = suscripcionMapper.toDto(suscripcion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuscripcionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(suscripcionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Suscripcion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSuscripcion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        suscripcion.setId(longCount.incrementAndGet());

        // Create the Suscripcion
        SuscripcionDTO suscripcionDTO = suscripcionMapper.toDto(suscripcion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuscripcionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(suscripcionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Suscripcion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSuscripcion() throws Exception {
        // Initialize the database
        insertedSuscripcion = suscripcionRepository.saveAndFlush(suscripcion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the suscripcion
        restSuscripcionMockMvc
            .perform(delete(ENTITY_API_URL_ID, suscripcion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return suscripcionRepository.count();
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

    protected Suscripcion getPersistedSuscripcion(Suscripcion suscripcion) {
        return suscripcionRepository.findById(suscripcion.getId()).orElseThrow();
    }

    protected void assertPersistedSuscripcionToMatchAllProperties(Suscripcion expectedSuscripcion) {
        assertSuscripcionAllPropertiesEquals(expectedSuscripcion, getPersistedSuscripcion(expectedSuscripcion));
    }

    protected void assertPersistedSuscripcionToMatchUpdatableProperties(Suscripcion expectedSuscripcion) {
        assertSuscripcionAllUpdatablePropertiesEquals(expectedSuscripcion, getPersistedSuscripcion(expectedSuscripcion));
    }
}
