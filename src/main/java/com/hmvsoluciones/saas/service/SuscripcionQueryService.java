package com.hmvsoluciones.saas.service;

import com.hmvsoluciones.saas.domain.*; // for static metamodels
import com.hmvsoluciones.saas.domain.Suscripcion;
import com.hmvsoluciones.saas.repository.SuscripcionRepository;
import com.hmvsoluciones.saas.service.criteria.SuscripcionCriteria;
import com.hmvsoluciones.saas.service.dto.SuscripcionDTO;
import com.hmvsoluciones.saas.service.mapper.SuscripcionMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Suscripcion} entities in the database.
 * The main input is a {@link SuscripcionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SuscripcionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SuscripcionQueryService extends QueryService<Suscripcion> {

    private static final Logger LOG = LoggerFactory.getLogger(SuscripcionQueryService.class);

    private final SuscripcionRepository suscripcionRepository;

    private final SuscripcionMapper suscripcionMapper;

    public SuscripcionQueryService(SuscripcionRepository suscripcionRepository, SuscripcionMapper suscripcionMapper) {
        this.suscripcionRepository = suscripcionRepository;
        this.suscripcionMapper = suscripcionMapper;
    }

    /**
     * Return a {@link Page} of {@link SuscripcionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SuscripcionDTO> findByCriteria(SuscripcionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Suscripcion> specification = createSpecification(criteria);
        return suscripcionRepository.findAll(specification, page).map(suscripcionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SuscripcionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Suscripcion> specification = createSpecification(criteria);
        return suscripcionRepository.count(specification);
    }

    /**
     * Function to convert {@link SuscripcionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Suscripcion> createSpecification(SuscripcionCriteria criteria) {
        Specification<Suscripcion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Suscripcion_.id),
                buildRangeSpecification(criteria.getFechaInicio(), Suscripcion_.fechaInicio),
                buildRangeSpecification(criteria.getFechaFin(), Suscripcion_.fechaFin),
                buildSpecification(criteria.getEstado(), Suscripcion_.estado),
                buildSpecification(criteria.getClienteId(), root -> root.join(Suscripcion_.cliente, JoinType.LEFT).get(Cliente_.id)),
                buildSpecification(criteria.getPlanId(), root -> root.join(Suscripcion_.plan, JoinType.LEFT).get(Plan_.id)),
                buildSpecification(criteria.getPagoId(), root -> root.join(Suscripcion_.pagos, JoinType.LEFT).get(Pago_.id))
            );
        }
        return specification;
    }
}
