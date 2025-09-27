<template>
  <div>
    <h2 id="page-heading" data-cy="TipoProductoHeading">
      <span id="tipo-producto-heading">Tipo Productos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'TipoProductoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-tipo-producto"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Tipo Producto</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && tipoProductos && tipoProductos.length === 0">
      <span>Ningún Tipo Productos encontrado</span>
    </div>
    <div class="table-responsive" v-if="tipoProductos && tipoProductos.length > 0">
      <table class="table table-striped" aria-describedby="tipoProductos">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nombre')">
              <span>Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="tipoProducto in tipoProductos" :key="tipoProducto.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TipoProductoView', params: { tipoProductoId: tipoProducto.id } }">{{
                tipoProducto.id
              }}</router-link>
            </td>
            <td>{{ tipoProducto.nombre }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'TipoProductoView', params: { tipoProductoId: tipoProducto.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TipoProductoEdit', params: { tipoProductoId: tipoProducto.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(tipoProducto)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Eliminar</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="hmvsolucionesSaasMvcApp.tipoProducto.delete.question" data-cy="tipoProductoDeleteDialogHeading"
          >Confirmar operación de borrado</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-tipoProducto-heading">¿Seguro que quiere eliminar Tipo Producto {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-tipoProducto"
            data-cy="entityConfirmDeleteButton"
            @click="removeTipoProducto()"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="tipoProductos && tipoProductos.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./tipo-producto.component.ts"></script>
