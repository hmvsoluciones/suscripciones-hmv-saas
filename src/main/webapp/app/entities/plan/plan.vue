<template>
  <div>
    <h2 id="page-heading" data-cy="PlanHeading">
      <span id="plan-heading">Plans</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refrescar lista</span>
        </button>
        <router-link :to="{ name: 'PlanCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-plan">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Crear nuevo Plan</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && plans && plans.length === 0">
      <span>Ningún Plans encontrado</span>
    </div>
    <div class="table-responsive" v-if="plans && plans.length > 0">
      <table class="table table-striped" aria-describedby="plans">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nombre')">
              <span>Nombre</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('descripcion')">
              <span>Descripcion</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'descripcion'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('precio')">
              <span>Precio</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'precio'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('duracionMeses')">
              <span>Duracion Meses</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'duracionMeses'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('tipoPago')">
              <span>Tipo Pago</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tipoPago'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('producto.id')">
              <span>Producto</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'producto.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="plan in plans" :key="plan.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PlanView', params: { planId: plan.id } }">{{ plan.id }}</router-link>
            </td>
            <td>{{ plan.nombre }}</td>
            <td>{{ plan.descripcion }}</td>
            <td>{{ plan.precio }}</td>
            <td>{{ plan.duracionMeses }}</td>
            <td>{{ plan.tipoPago }}</td>
            <td>
              <div v-if="plan.producto">
                <router-link :to="{ name: 'ProductoView', params: { productoId: plan.producto.id } }">{{ plan.producto.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'PlanView', params: { planId: plan.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Vista</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PlanEdit', params: { planId: plan.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(plan)"
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
        <span id="hmvsolucionesSaasMvcApp.plan.delete.question" data-cy="planDeleteDialogHeading">Confirmar operación de borrado</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-plan-heading">¿Seguro que quiere eliminar Plan {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-plan"
            data-cy="entityConfirmDeleteButton"
            @click="removePlan()"
          >
            Eliminar
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="plans && plans.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./plan.component.ts"></script>
