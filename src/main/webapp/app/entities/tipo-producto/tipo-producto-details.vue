<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="tipoProducto">
        <h2 class="jh-entity-heading" data-cy="tipoProductoDetailsHeading"><span>Tipo Producto</span> {{ tipoProducto.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ tipoProducto.nombre }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="tipoProducto.id"
          :to="{ name: 'TipoProductoEdit', params: { tipoProductoId: tipoProducto.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./tipo-producto-details.component.ts"></script>
