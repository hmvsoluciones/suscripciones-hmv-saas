<template>
  <div>
    <b-dropdown-item to="/cliente">
      <font-awesome-icon icon="asterisk" />
      <span>Cliente</span>
    </b-dropdown-item>
    <b-dropdown-item to="/tipo-producto">
      <font-awesome-icon icon="asterisk" />
      <span>Tipo Producto</span>
    </b-dropdown-item>
    <b-dropdown-item to="/producto">
      <font-awesome-icon icon="asterisk" />
      <span>Producto</span>
    </b-dropdown-item>
    <b-dropdown-item to="/plan">
      <font-awesome-icon icon="asterisk" />
      <span>Plan</span>
    </b-dropdown-item>
    <b-dropdown-item to="/suscripcion">
      <font-awesome-icon icon="asterisk" />
      <span>Suscripcion</span>
    </b-dropdown-item>
    <b-dropdown-item to="/pago">
      <font-awesome-icon icon="asterisk" />
      <span>Pago</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
