package com.hmvsoluciones.saas.service;

import com.hmvsoluciones.saas.domain.*; // for static metamodels
import com.hmvsoluciones.saas.domain.TipoProducto;
import com.hmvsoluciones.saas.repository.TipoProductoRepository;
import com.hmvsoluciones.saas.service.criteria.TipoProductoCriteria;
import com.hmvsoluciones.saas.service.dto.TipoProductoDTO;
import com.hmvsoluciones.saas.service.mapper.TipoProductoMapper;
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
 * Service for executing complex queries for {@link TipoProducto} entities in the database.
 * The main input is a {@link TipoProductoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TipoProductoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoProductoQueryService extends QueryService<TipoProducto> {

    private static final Logger LOG = LoggerFactory.getLogger(TipoProductoQueryService.class);

    private final TipoProductoRepository tipoProductoRepository;

    private final TipoProductoMapper tipoProductoMapper;

    public TipoProductoQueryService(TipoProductoRepository tipoProductoRepository, TipoProductoMapper tipoProductoMapper) {
        this.tipoProductoRepository = tipoProductoRepository;
        this.tipoProductoMapper = tipoProductoMapper;
    }

    /**
     * Return a {@link Page} of {@link TipoProductoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoProductoDTO> findByCriteria(TipoProductoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TipoProducto> specification = createSpecification(criteria);
        return tipoProductoRepository.findAll(specification, page).map(tipoProductoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoProductoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TipoProducto> specification = createSpecification(criteria);
        return tipoProductoRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoProductoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TipoProducto> createSpecification(TipoProductoCriteria criteria) {
        Specification<TipoProducto> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TipoProducto_.id),
                buildStringSpecification(criteria.getNombre(), TipoProducto_.nombre),
                buildSpecification(criteria.getProductoId(), root -> root.join(TipoProducto_.productos, JoinType.LEFT).get(Producto_.id))
            );
        }
        return specification;
    }
}
