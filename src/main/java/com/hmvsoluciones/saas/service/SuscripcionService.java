package com.hmvsoluciones.saas.service;

import com.hmvsoluciones.saas.domain.Suscripcion;
import com.hmvsoluciones.saas.repository.SuscripcionRepository;
import com.hmvsoluciones.saas.service.dto.SuscripcionDTO;
import com.hmvsoluciones.saas.service.mapper.SuscripcionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.hmvsoluciones.saas.domain.Suscripcion}.
 */
@Service
@Transactional
public class SuscripcionService {

    private static final Logger LOG = LoggerFactory.getLogger(SuscripcionService.class);

    private final SuscripcionRepository suscripcionRepository;

    private final SuscripcionMapper suscripcionMapper;

    public SuscripcionService(SuscripcionRepository suscripcionRepository, SuscripcionMapper suscripcionMapper) {
        this.suscripcionRepository = suscripcionRepository;
        this.suscripcionMapper = suscripcionMapper;
    }

    /**
     * Save a suscripcion.
     *
     * @param suscripcionDTO the entity to save.
     * @return the persisted entity.
     */
    public SuscripcionDTO save(SuscripcionDTO suscripcionDTO) {
        LOG.debug("Request to save Suscripcion : {}", suscripcionDTO);
        Suscripcion suscripcion = suscripcionMapper.toEntity(suscripcionDTO);
        suscripcion = suscripcionRepository.save(suscripcion);
        return suscripcionMapper.toDto(suscripcion);
    }

    /**
     * Update a suscripcion.
     *
     * @param suscripcionDTO the entity to save.
     * @return the persisted entity.
     */
    public SuscripcionDTO update(SuscripcionDTO suscripcionDTO) {
        LOG.debug("Request to update Suscripcion : {}", suscripcionDTO);
        Suscripcion suscripcion = suscripcionMapper.toEntity(suscripcionDTO);
        suscripcion = suscripcionRepository.save(suscripcion);
        return suscripcionMapper.toDto(suscripcion);
    }

    /**
     * Partially update a suscripcion.
     *
     * @param suscripcionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SuscripcionDTO> partialUpdate(SuscripcionDTO suscripcionDTO) {
        LOG.debug("Request to partially update Suscripcion : {}", suscripcionDTO);

        return suscripcionRepository
            .findById(suscripcionDTO.getId())
            .map(existingSuscripcion -> {
                suscripcionMapper.partialUpdate(existingSuscripcion, suscripcionDTO);

                return existingSuscripcion;
            })
            .map(suscripcionRepository::save)
            .map(suscripcionMapper::toDto);
    }

    /**
     * Get one suscripcion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SuscripcionDTO> findOne(Long id) {
        LOG.debug("Request to get Suscripcion : {}", id);
        return suscripcionRepository.findById(id).map(suscripcionMapper::toDto);
    }

    /**
     * Delete the suscripcion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Suscripcion : {}", id);
        suscripcionRepository.deleteById(id);
    }
}
