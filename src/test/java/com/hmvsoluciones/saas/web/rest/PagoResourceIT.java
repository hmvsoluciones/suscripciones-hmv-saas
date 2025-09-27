package com.hmvsoluciones.saas.web.rest;

import static com.hmvsoluciones.saas.domain.PagoAsserts.*;
import static com.hmvsoluciones.saas.web.rest.TestUtil.createUpdateProxyForBean;
import static com.hmvsoluciones.saas.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmvsoluciones.saas.IntegrationTest;
import com.hmvsoluciones.saas.domain.Pago;
import com.hmvsoluciones.saas.domain.Suscripcion;
import com.hmvsoluciones.saas.domain.enumeration.MetodoPago;
import com.hmvsoluciones.saas.repository.PagoRepository;
import com.hmvsoluciones.saas.service.dto.PagoDTO;
import com.hmvsoluciones.saas.service.mapper.PagoMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PagoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PagoResourceIT {

    private static final LocalDate DEFAULT_FECHA_PAGO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_PAGO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_PAGO = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_MONTO = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONTO = new BigDecimal(1);
    private static final BigDecimal SMALLER_MONTO = new BigDecimal(0 - 1);

    private static final MetodoPago DEFAULT_METODO_PAGO = MetodoPago.TARJETA;
    private static final MetodoPago UPDATED_METODO_PAGO = MetodoPago.TRANSFERENCIA;

    private static final String DEFAULT_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pagos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PagoMapper pagoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPagoMockMvc;

    private Pago pago;

    private Pago insertedPago;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pago createEntity() {
        return new Pago().fechaPago(DEFAULT_FECHA_PAGO).monto(DEFAULT_MONTO).metodoPago(DEFAULT_METODO_PAGO).referencia(DEFAULT_REFERENCIA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pago createUpdatedEntity() {
        return new Pago().fechaPago(UPDATED_FECHA_PAGO).monto(UPDATED_MONTO).metodoPago(UPDATED_METODO_PAGO).referencia(UPDATED_REFERENCIA);
    }

    @BeforeEach
    void initTest() {
        pago = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPago != null) {
            pagoRepository.delete(insertedPago);
            insertedPago = null;
        }
    }

    @Test
    @Transactional
    void createPago() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);
        var returnedPagoDTO = om.readValue(
            restPagoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pagoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PagoDTO.class
        );

        // Validate the Pago in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPago = pagoMapper.toEntity(returnedPagoDTO);
        assertPagoUpdatableFieldsEquals(returnedPago, getPersistedPago(returnedPago));

        insertedPago = returnedPago;
    }

    @Test
    @Transactional
    void createPagoWithExistingId() throws Exception {
        // Create the Pago with an existing ID
        pago.setId(1L);
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pagoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaPagoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pago.setFechaPago(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pagoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pago.setMonto(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pagoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMetodoPagoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pago.setMetodoPago(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pagoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPagos() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList
        restPagoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pago.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaPago").value(hasItem(DEFAULT_FECHA_PAGO.toString())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(sameNumber(DEFAULT_MONTO))))
            .andExpect(jsonPath("$.[*].metodoPago").value(hasItem(DEFAULT_METODO_PAGO.toString())))
            .andExpect(jsonPath("$.[*].referencia").value(hasItem(DEFAULT_REFERENCIA)));
    }

    @Test
    @Transactional
    void getPago() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get the pago
        restPagoMockMvc
            .perform(get(ENTITY_API_URL_ID, pago.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pago.getId().intValue()))
            .andExpect(jsonPath("$.fechaPago").value(DEFAULT_FECHA_PAGO.toString()))
            .andExpect(jsonPath("$.monto").value(sameNumber(DEFAULT_MONTO)))
            .andExpect(jsonPath("$.metodoPago").value(DEFAULT_METODO_PAGO.toString()))
            .andExpect(jsonPath("$.referencia").value(DEFAULT_REFERENCIA));
    }

    @Test
    @Transactional
    void getPagosByIdFiltering() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        Long id = pago.getId();

        defaultPagoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPagoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPagoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where fechaPago equals to
        defaultPagoFiltering("fechaPago.equals=" + DEFAULT_FECHA_PAGO, "fechaPago.equals=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where fechaPago in
        defaultPagoFiltering("fechaPago.in=" + DEFAULT_FECHA_PAGO + "," + UPDATED_FECHA_PAGO, "fechaPago.in=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where fechaPago is not null
        defaultPagoFiltering("fechaPago.specified=true", "fechaPago.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where fechaPago is greater than or equal to
        defaultPagoFiltering("fechaPago.greaterThanOrEqual=" + DEFAULT_FECHA_PAGO, "fechaPago.greaterThanOrEqual=" + UPDATED_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where fechaPago is less than or equal to
        defaultPagoFiltering("fechaPago.lessThanOrEqual=" + DEFAULT_FECHA_PAGO, "fechaPago.lessThanOrEqual=" + SMALLER_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where fechaPago is less than
        defaultPagoFiltering("fechaPago.lessThan=" + UPDATED_FECHA_PAGO, "fechaPago.lessThan=" + DEFAULT_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByFechaPagoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where fechaPago is greater than
        defaultPagoFiltering("fechaPago.greaterThan=" + SMALLER_FECHA_PAGO, "fechaPago.greaterThan=" + DEFAULT_FECHA_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByMontoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where monto equals to
        defaultPagoFiltering("monto.equals=" + DEFAULT_MONTO, "monto.equals=" + UPDATED_MONTO);
    }

    @Test
    @Transactional
    void getAllPagosByMontoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where monto in
        defaultPagoFiltering("monto.in=" + DEFAULT_MONTO + "," + UPDATED_MONTO, "monto.in=" + UPDATED_MONTO);
    }

    @Test
    @Transactional
    void getAllPagosByMontoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where monto is not null
        defaultPagoFiltering("monto.specified=true", "monto.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByMontoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where monto is greater than or equal to
        defaultPagoFiltering("monto.greaterThanOrEqual=" + DEFAULT_MONTO, "monto.greaterThanOrEqual=" + UPDATED_MONTO);
    }

    @Test
    @Transactional
    void getAllPagosByMontoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where monto is less than or equal to
        defaultPagoFiltering("monto.lessThanOrEqual=" + DEFAULT_MONTO, "monto.lessThanOrEqual=" + SMALLER_MONTO);
    }

    @Test
    @Transactional
    void getAllPagosByMontoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where monto is less than
        defaultPagoFiltering("monto.lessThan=" + UPDATED_MONTO, "monto.lessThan=" + DEFAULT_MONTO);
    }

    @Test
    @Transactional
    void getAllPagosByMontoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where monto is greater than
        defaultPagoFiltering("monto.greaterThan=" + SMALLER_MONTO, "monto.greaterThan=" + DEFAULT_MONTO);
    }

    @Test
    @Transactional
    void getAllPagosByMetodoPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where metodoPago equals to
        defaultPagoFiltering("metodoPago.equals=" + DEFAULT_METODO_PAGO, "metodoPago.equals=" + UPDATED_METODO_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByMetodoPagoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where metodoPago in
        defaultPagoFiltering("metodoPago.in=" + DEFAULT_METODO_PAGO + "," + UPDATED_METODO_PAGO, "metodoPago.in=" + UPDATED_METODO_PAGO);
    }

    @Test
    @Transactional
    void getAllPagosByMetodoPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where metodoPago is not null
        defaultPagoFiltering("metodoPago.specified=true", "metodoPago.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where referencia equals to
        defaultPagoFiltering("referencia.equals=" + DEFAULT_REFERENCIA, "referencia.equals=" + UPDATED_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPagosByReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where referencia in
        defaultPagoFiltering("referencia.in=" + DEFAULT_REFERENCIA + "," + UPDATED_REFERENCIA, "referencia.in=" + UPDATED_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPagosByReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where referencia is not null
        defaultPagoFiltering("referencia.specified=true", "referencia.specified=false");
    }

    @Test
    @Transactional
    void getAllPagosByReferenciaContainsSomething() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where referencia contains
        defaultPagoFiltering("referencia.contains=" + DEFAULT_REFERENCIA, "referencia.contains=" + UPDATED_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPagosByReferenciaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        // Get all the pagoList where referencia does not contain
        defaultPagoFiltering("referencia.doesNotContain=" + UPDATED_REFERENCIA, "referencia.doesNotContain=" + DEFAULT_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPagosBySuscripcionIsEqualToSomething() throws Exception {
        Suscripcion suscripcion;
        if (TestUtil.findAll(em, Suscripcion.class).isEmpty()) {
            pagoRepository.saveAndFlush(pago);
            suscripcion = SuscripcionResourceIT.createEntity();
        } else {
            suscripcion = TestUtil.findAll(em, Suscripcion.class).get(0);
        }
        em.persist(suscripcion);
        em.flush();
        pago.setSuscripcion(suscripcion);
        pagoRepository.saveAndFlush(pago);
        Long suscripcionId = suscripcion.getId();
        // Get all the pagoList where suscripcion equals to suscripcionId
        defaultPagoShouldBeFound("suscripcionId.equals=" + suscripcionId);

        // Get all the pagoList where suscripcion equals to (suscripcionId + 1)
        defaultPagoShouldNotBeFound("suscripcionId.equals=" + (suscripcionId + 1));
    }

    private void defaultPagoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPagoShouldBeFound(shouldBeFound);
        defaultPagoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPagoShouldBeFound(String filter) throws Exception {
        restPagoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pago.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaPago").value(hasItem(DEFAULT_FECHA_PAGO.toString())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(sameNumber(DEFAULT_MONTO))))
            .andExpect(jsonPath("$.[*].metodoPago").value(hasItem(DEFAULT_METODO_PAGO.toString())))
            .andExpect(jsonPath("$.[*].referencia").value(hasItem(DEFAULT_REFERENCIA)));

        // Check, that the count call also returns 1
        restPagoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPagoShouldNotBeFound(String filter) throws Exception {
        restPagoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPagoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPago() throws Exception {
        // Get the pago
        restPagoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPago() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pago
        Pago updatedPago = pagoRepository.findById(pago.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPago are not directly saved in db
        em.detach(updatedPago);
        updatedPago.fechaPago(UPDATED_FECHA_PAGO).monto(UPDATED_MONTO).metodoPago(UPDATED_METODO_PAGO).referencia(UPDATED_REFERENCIA);
        PagoDTO pagoDTO = pagoMapper.toDto(updatedPago);

        restPagoMockMvc
            .perform(put(ENTITY_API_URL_ID, pagoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pagoDTO)))
            .andExpect(status().isOk());

        // Validate the Pago in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPagoToMatchAllProperties(updatedPago);
    }

    @Test
    @Transactional
    void putNonExistingPago() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pago.setId(longCount.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(put(ENTITY_API_URL_ID, pagoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pagoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPago() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pago.setId(longCount.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pagoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPago() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pago.setId(longCount.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pagoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pago in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePagoWithPatch() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pago using partial update
        Pago partialUpdatedPago = new Pago();
        partialUpdatedPago.setId(pago.getId());

        partialUpdatedPago.fechaPago(UPDATED_FECHA_PAGO).monto(UPDATED_MONTO).metodoPago(UPDATED_METODO_PAGO);

        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPago.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPago))
            )
            .andExpect(status().isOk());

        // Validate the Pago in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPagoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPago, pago), getPersistedPago(pago));
    }

    @Test
    @Transactional
    void fullUpdatePagoWithPatch() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pago using partial update
        Pago partialUpdatedPago = new Pago();
        partialUpdatedPago.setId(pago.getId());

        partialUpdatedPago
            .fechaPago(UPDATED_FECHA_PAGO)
            .monto(UPDATED_MONTO)
            .metodoPago(UPDATED_METODO_PAGO)
            .referencia(UPDATED_REFERENCIA);

        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPago.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPago))
            )
            .andExpect(status().isOk());

        // Validate the Pago in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPagoUpdatableFieldsEquals(partialUpdatedPago, getPersistedPago(partialUpdatedPago));
    }

    @Test
    @Transactional
    void patchNonExistingPago() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pago.setId(longCount.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pagoDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pagoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPago() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pago.setId(longCount.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pagoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPago() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pago.setId(longCount.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pagoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pago in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePago() throws Exception {
        // Initialize the database
        insertedPago = pagoRepository.saveAndFlush(pago);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pago
        restPagoMockMvc
            .perform(delete(ENTITY_API_URL_ID, pago.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pagoRepository.count();
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

    protected Pago getPersistedPago(Pago pago) {
        return pagoRepository.findById(pago.getId()).orElseThrow();
    }

    protected void assertPersistedPagoToMatchAllProperties(Pago expectedPago) {
        assertPagoAllPropertiesEquals(expectedPago, getPersistedPago(expectedPago));
    }

    protected void assertPersistedPagoToMatchUpdatableProperties(Pago expectedPago) {
        assertPagoAllUpdatablePropertiesEquals(expectedPago, getPersistedPago(expectedPago));
    }
}
