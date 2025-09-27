package com.hmvsoluciones.saas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @NotNull
    @Size(max = 256)
    @Column(name = "email", length = 256, nullable = false)
    private String email;

    @Size(max = 30)
    @Column(name = "telefono", length = 30)
    private String telefono;

    @Size(max = 150)
    @Column(name = "razon_social", length = 150)
    private String razonSocial;

    @Size(max = 13)
    @Column(name = "rfc", length = 13)
    private String rfc;

    @Column(name = "activo")
    private Boolean activo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    @JsonIgnoreProperties(value = { "cliente", "plan", "pagos" }, allowSetters = true)
    private Set<Suscripcion> suscripcions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Cliente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return this.email;
    }

    public Cliente email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Cliente telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRazonSocial() {
        return this.razonSocial;
    }

    public Cliente razonSocial(String razonSocial) {
        this.setRazonSocial(razonSocial);
        return this;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRfc() {
        return this.rfc;
    }

    public Cliente rfc(String rfc) {
        this.setRfc(rfc);
        return this;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Cliente activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Suscripcion> getSuscripcions() {
        return this.suscripcions;
    }

    public void setSuscripcions(Set<Suscripcion> suscripcions) {
        if (this.suscripcions != null) {
            this.suscripcions.forEach(i -> i.setCliente(null));
        }
        if (suscripcions != null) {
            suscripcions.forEach(i -> i.setCliente(this));
        }
        this.suscripcions = suscripcions;
    }

    public Cliente suscripcions(Set<Suscripcion> suscripcions) {
        this.setSuscripcions(suscripcions);
        return this;
    }

    public Cliente addSuscripcion(Suscripcion suscripcion) {
        this.suscripcions.add(suscripcion);
        suscripcion.setCliente(this);
        return this;
    }

    public Cliente removeSuscripcion(Suscripcion suscripcion) {
        this.suscripcions.remove(suscripcion);
        suscripcion.setCliente(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return getId() != null && getId().equals(((Cliente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", razonSocial='" + getRazonSocial() + "'" +
            ", rfc='" + getRfc() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
