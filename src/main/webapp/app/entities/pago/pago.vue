<template>
  <div>
    <h2 id="page-heading" data-cy="PagoHeading">
      <span id="pago-heading">Pagos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'PagoCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-pago">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Pago</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && pagos && pagos.length === 0">
      <span>Ningún Pagos encontrado</span>
    </div>
    <div class="table-responsive" v-if="pagos && pagos.length > 0">
      <table class="table table-striped" aria-describedby="pagos">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fechaPago')">
              <span>Fecha Pago</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaPago'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('monto')">
              <span>Monto</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'monto'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('metodoPago')">
              <span>Metodo Pago</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'metodoPago'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('referencia')">
              <span>Referencia</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'referencia'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('suscripcion.id')">
              <span>Suscripcion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'suscripcion.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="pago in pagos" :key="pago.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PagoView', params: { pagoId: pago.id } }">{{ pago.id }}</router-link>
            </td>
            <td>{{ pago.fechaPago }}</td>
            <td>{{ pago.monto }}</td>
            <td>{{ pago.metodoPago }}</td>
            <td>{{ pago.referencia }}</td>
            <td>
              <div v-if="pago.suscripcion">
                <router-link :to="{ name: 'SuscripcionView', params: { suscripcionId: pago.suscripcion.id } }">
                  {{ pago.suscripcion.nombre }} - {{ pago.suscripcion.cliente.nombre }} - {{ pago.suscripcion.plan.nombre }}
                </router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'PagoView', params: { pagoId: pago.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PagoEdit', params: { pagoId: pago.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(pago)"
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
        <span id="hmvsolucionesSaasMvcApp.pago.delete.question" data-cy="pagoDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-pago-heading">¿Seguro que quiere eliminar Pago {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-pago"
            data-cy="entityConfirmDeleteButton"
            @click="removePago()"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="pagos && pagos.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./pago.component.ts"></script>
