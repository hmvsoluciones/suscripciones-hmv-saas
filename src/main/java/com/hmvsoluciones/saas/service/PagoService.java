package com.hmvsoluciones.saas.service;

import com.hmvsoluciones.saas.domain.Pago;
import com.hmvsoluciones.saas.repository.PagoRepository;
import com.hmvsoluciones.saas.service.dto.PagoDTO;
import com.hmvsoluciones.saas.service.mapper.PagoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.hmvsoluciones.saas.domain.Pago}.
 */
@Service
@Transactional
public class PagoService {

    private static final Logger LOG = LoggerFactory.getLogger(PagoService.class);

    private final PagoRepository pagoRepository;

    private final PagoMapper pagoMapper;

    public PagoService(PagoRepository pagoRepository, PagoMapper pagoMapper) {
        this.pagoRepository = pagoRepository;
        this.pagoMapper = pagoMapper;
    }

    /**
     * Save a pago.
     *
     * @param pagoDTO the entity to save.
     * @return the persisted entity.
     */
    public PagoDTO save(PagoDTO pagoDTO) {
        LOG.debug("Request to save Pago : {}", pagoDTO);
        Pago pago = pagoMapper.toEntity(pagoDTO);
        pago = pagoRepository.save(pago);
        return pagoMapper.toDto(pago);
    }

    /**
     * Update a pago.
     *
     * @param pagoDTO the entity to save.
     * @return the persisted entity.
     */
    public PagoDTO update(PagoDTO pagoDTO) {
        LOG.debug("Request to update Pago : {}", pagoDTO);
        Pago pago = pagoMapper.toEntity(pagoDTO);
        pago = pagoRepository.save(pago);
        return pagoMapper.toDto(pago);
    }

    /**
     * Partially update a pago.
     *
     * @param pagoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PagoDTO> partialUpdate(PagoDTO pagoDTO) {
        LOG.debug("Request to partially update Pago : {}", pagoDTO);

        return pagoRepository
            .findById(pagoDTO.getId())
            .map(existingPago -> {
                pagoMapper.partialUpdate(existingPago, pagoDTO);

                return existingPago;
            })
            .map(pagoRepository::save)
            .map(pagoMapper::toDto);
    }

    /**
     * Get one pago by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PagoDTO> findOne(Long id) {
        LOG.debug("Request to get Pago : {}", id);
        return pagoRepository.findById(id).map(pagoMapper::toDto);
    }

    /**
     * Delete the pago by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Pago : {}", id);
        pagoRepository.deleteById(id);
    }
}
