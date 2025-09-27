package com.hmvsoluciones.saas.service.criteria;

import com.hmvsoluciones.saas.domain.enumeration.EstadoSuscripcion;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hmvsoluciones.saas.domain.Suscripcion} entity. This class is used
 * in {@link com.hmvsoluciones.saas.web.rest.SuscripcionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /suscripcions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SuscripcionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EstadoSuscripcion
     */
    public static class EstadoSuscripcionFilter extends Filter<EstadoSuscripcion> {

        public EstadoSuscripcionFilter() {}

        public EstadoSuscripcionFilter(EstadoSuscripcionFilter filter) {
            super(filter);
        }

        @Override
        public EstadoSuscripcionFilter copy() {
            return new EstadoSuscripcionFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fechaInicio;

    private LocalDateFilter fechaFin;

    private EstadoSuscripcionFilter estado;

    private LongFilter clienteId;

    private LongFilter planId;

    private LongFilter pagoId;

    private Boolean distinct;

    public SuscripcionCriteria() {}

    public SuscripcionCriteria(SuscripcionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fechaInicio = other.optionalFechaInicio().map(LocalDateFilter::copy).orElse(null);
        this.fechaFin = other.optionalFechaFin().map(LocalDateFilter::copy).orElse(null);
        this.estado = other.optionalEstado().map(EstadoSuscripcionFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.planId = other.optionalPlanId().map(LongFilter::copy).orElse(null);
        this.pagoId = other.optionalPagoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SuscripcionCriteria copy() {
        return new SuscripcionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getFechaInicio() {
        return fechaInicio;
    }

    public Optional<LocalDateFilter> optionalFechaInicio() {
        return Optional.ofNullable(fechaInicio);
    }

    public LocalDateFilter fechaInicio() {
        if (fechaInicio == null) {
            setFechaInicio(new LocalDateFilter());
        }
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateFilter fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateFilter getFechaFin() {
        return fechaFin;
    }

    public Optional<LocalDateFilter> optionalFechaFin() {
        return Optional.ofNullable(fechaFin);
    }

    public LocalDateFilter fechaFin() {
        if (fechaFin == null) {
            setFechaFin(new LocalDateFilter());
        }
        return fechaFin;
    }

    public void setFechaFin(LocalDateFilter fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoSuscripcionFilter getEstado() {
        return estado;
    }

    public Optional<EstadoSuscripcionFilter> optionalEstado() {
        return Optional.ofNullable(estado);
    }

    public EstadoSuscripcionFilter estado() {
        if (estado == null) {
            setEstado(new EstadoSuscripcionFilter());
        }
        return estado;
    }

    public void setEstado(EstadoSuscripcionFilter estado) {
        this.estado = estado;
    }

    public LongFilter getClienteId() {
        return clienteId;
    }

    public Optional<LongFilter> optionalClienteId() {
        return Optional.ofNullable(clienteId);
    }

    public LongFilter clienteId() {
        if (clienteId == null) {
            setClienteId(new LongFilter());
        }
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
    }

    public LongFilter getPlanId() {
        return planId;
    }

    public Optional<LongFilter> optionalPlanId() {
        return Optional.ofNullable(planId);
    }

    public LongFilter planId() {
        if (planId == null) {
            setPlanId(new LongFilter());
        }
        return planId;
    }

    public void setPlanId(LongFilter planId) {
        this.planId = planId;
    }

    public LongFilter getPagoId() {
        return pagoId;
    }

    public Optional<LongFilter> optionalPagoId() {
        return Optional.ofNullable(pagoId);
    }

    public LongFilter pagoId() {
        if (pagoId == null) {
            setPagoId(new LongFilter());
        }
        return pagoId;
    }

    public void setPagoId(LongFilter pagoId) {
        this.pagoId = pagoId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SuscripcionCriteria that = (SuscripcionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fechaInicio, that.fechaInicio) &&
            Objects.equals(fechaFin, that.fechaFin) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(planId, that.planId) &&
            Objects.equals(pagoId, that.pagoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaInicio, fechaFin, estado, clienteId, planId, pagoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuscripcionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFechaInicio().map(f -> "fechaInicio=" + f + ", ").orElse("") +
            optionalFechaFin().map(f -> "fechaFin=" + f + ", ").orElse("") +
            optionalEstado().map(f -> "estado=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalPlanId().map(f -> "planId=" + f + ", ").orElse("") +
            optionalPagoId().map(f -> "pagoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
