<template>
  <div>
    <h2 id="page-heading" data-cy="SuscripcionHeading">
      <span id="suscripcion-heading">Suscripcions</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'SuscripcionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-suscripcion"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Suscripcion</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && suscripcions && suscripcions.length === 0">
      <span>Ningún Suscripcions encontrado</span>
    </div>
    <div class="table-responsive" v-if="suscripcions && suscripcions.length > 0">
      <table class="table table-striped" aria-describedby="suscripcions">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fechaInicio')">
              <span>Fecha Inicio</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaInicio'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fechaFin')">
              <span>Fecha Fin</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaFin'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('estado')">
              <span>Estado</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'estado'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('cliente.id')">
              <span>Cliente</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cliente.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('plan.id')">
              <span>Plan</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'plan.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="suscripcion in suscripcions" :key="suscripcion.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SuscripcionView', params: { suscripcionId: suscripcion.id } }">{{ suscripcion.id }}</router-link>
            </td>
            <td>{{ suscripcion.fechaInicio }}</td>
            <td>{{ suscripcion.fechaFin }}</td>
            <td>{{ suscripcion.estado }}</td>
            <td>
              <div v-if="suscripcion.cliente">
                <router-link :to="{ name: 'ClienteView', params: { clienteId: suscripcion.cliente.id } }">
                  {{ suscripcion.cliente.nombre }} - {{ suscripcion.cliente.razonSocial }}
                </router-link>
              </div>
            </td>
            <td>
              <div v-if="suscripcion.plan">
                <router-link :to="{ name: 'PlanView', params: { planId: suscripcion.plan.id } }">{{ suscripcion.plan.nombre }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'SuscripcionView', params: { suscripcionId: suscripcion.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'SuscripcionEdit', params: { suscripcionId: suscripcion.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(suscripcion)"
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
        <span id="hmvsolucionesSaasMvcApp.suscripcion.delete.question" data-cy="suscripcionDeleteDialogHeading"
          >Confirmar operación de borrado</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-suscripcion-heading">¿Seguro que quiere eliminar Suscripcion {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-suscripcion"
            data-cy="entityConfirmDeleteButton"
            @click="removeSuscripcion()"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="suscripcions && suscripcions.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./suscripcion.component.ts"></script>
