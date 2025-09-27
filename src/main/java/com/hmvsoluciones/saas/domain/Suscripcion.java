package com.hmvsoluciones.saas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hmvsoluciones.saas.domain.enumeration.EstadoSuscripcion;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Suscripcion.
 */
@Entity
@Table(name = "suscripcion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Suscripcion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoSuscripcion estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "suscripcions" }, allowSetters = true)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "producto", "suscripcions" }, allowSetters = true)
    private Plan plan;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "suscripcion")
    @JsonIgnoreProperties(value = { "suscripcion" }, allowSetters = true)
    private Set<Pago> pagos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Suscripcion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return this.fechaInicio;
    }

    public Suscripcion fechaInicio(LocalDate fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return this.fechaFin;
    }

    public Suscripcion fechaFin(LocalDate fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoSuscripcion getEstado() {
        return this.estado;
    }

    public Suscripcion estado(EstadoSuscripcion estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoSuscripcion estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Suscripcion cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public Plan getPlan() {
        return this.plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Suscripcion plan(Plan plan) {
        this.setPlan(plan);
        return this;
    }

    public Set<Pago> getPagos() {
        return this.pagos;
    }

    public void setPagos(Set<Pago> pagos) {
        if (this.pagos != null) {
            this.pagos.forEach(i -> i.setSuscripcion(null));
        }
        if (pagos != null) {
            pagos.forEach(i -> i.setSuscripcion(this));
        }
        this.pagos = pagos;
    }

    public Suscripcion pagos(Set<Pago> pagos) {
        this.setPagos(pagos);
        return this;
    }

    public Suscripcion addPago(Pago pago) {
        this.pagos.add(pago);
        pago.setSuscripcion(this);
        return this;
    }

    public Suscripcion removePago(Pago pago) {
        this.pagos.remove(pago);
        pago.setSuscripcion(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Suscripcion)) {
            return false;
        }
        return getId() != null && getId().equals(((Suscripcion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Suscripcion{" +
            "id=" + getId() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
