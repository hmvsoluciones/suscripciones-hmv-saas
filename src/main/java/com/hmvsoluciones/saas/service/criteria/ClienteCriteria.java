package com.hmvsoluciones.saas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.hmvsoluciones.saas.domain.Cliente} entity. This class is used
 * in {@link com.hmvsoluciones.saas.web.rest.ClienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter email;

    private StringFilter telefono;

    private StringFilter razonSocial;

    private StringFilter rfc;

    private BooleanFilter activo;

    private LongFilter suscripcionId;

    private Boolean distinct;

    public ClienteCriteria() {}

    public ClienteCriteria(ClienteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.telefono = other.optionalTelefono().map(StringFilter::copy).orElse(null);
        this.razonSocial = other.optionalRazonSocial().map(StringFilter::copy).orElse(null);
        this.rfc = other.optionalRfc().map(StringFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.suscripcionId = other.optionalSuscripcionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClienteCriteria copy() {
        return new ClienteCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelefono() {
        return telefono;
    }

    public Optional<StringFilter> optionalTelefono() {
        return Optional.ofNullable(telefono);
    }

    public StringFilter telefono() {
        if (telefono == null) {
            setTelefono(new StringFilter());
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
    }

    public StringFilter getRazonSocial() {
        return razonSocial;
    }

    public Optional<StringFilter> optionalRazonSocial() {
        return Optional.ofNullable(razonSocial);
    }

    public StringFilter razonSocial() {
        if (razonSocial == null) {
            setRazonSocial(new StringFilter());
        }
        return razonSocial;
    }

    public void setRazonSocial(StringFilter razonSocial) {
        this.razonSocial = razonSocial;
    }

    public StringFilter getRfc() {
        return rfc;
    }

    public Optional<StringFilter> optionalRfc() {
        return Optional.ofNullable(rfc);
    }

    public StringFilter rfc() {
        if (rfc == null) {
            setRfc(new StringFilter());
        }
        return rfc;
    }

    public void setRfc(StringFilter rfc) {
        this.rfc = rfc;
    }

    public BooleanFilter getActivo() {
        return activo;
    }

    public Optional<BooleanFilter> optionalActivo() {
        return Optional.ofNullable(activo);
    }

    public BooleanFilter activo() {
        if (activo == null) {
            setActivo(new BooleanFilter());
        }
        return activo;
    }

    public void setActivo(BooleanFilter activo) {
        this.activo = activo;
    }

    public LongFilter getSuscripcionId() {
        return suscripcionId;
    }

    public Optional<LongFilter> optionalSuscripcionId() {
        return Optional.ofNullable(suscripcionId);
    }

    public LongFilter suscripcionId() {
        if (suscripcionId == null) {
            setSuscripcionId(new LongFilter());
        }
        return suscripcionId;
    }

    public void setSuscripcionId(LongFilter suscripcionId) {
        this.suscripcionId = suscripcionId;
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
        final ClienteCriteria that = (ClienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(razonSocial, that.razonSocial) &&
            Objects.equals(rfc, that.rfc) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(suscripcionId, that.suscripcionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, email, telefono, razonSocial, rfc, activo, suscripcionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalTelefono().map(f -> "telefono=" + f + ", ").orElse("") +
            optionalRazonSocial().map(f -> "razonSocial=" + f + ", ").orElse("") +
            optionalRfc().map(f -> "rfc=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalSuscripcionId().map(f -> "suscripcionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
