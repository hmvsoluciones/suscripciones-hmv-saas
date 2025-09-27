<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="suscripcion">
        <h2 class="jh-entity-heading" data-cy="suscripcionDetailsHeading"><span>Suscripcion</span> {{ suscripcion.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Fecha Inicio</span>
          </dt>
          <dd>
            <span>{{ suscripcion.fechaInicio }}</span>
          </dd>
          <dt>
            <span>Fecha Fin</span>
          </dt>
          <dd>
            <span>{{ suscripcion.fechaFin }}</span>
          </dd>
          <dt>
            <span>Estado</span>
          </dt>
          <dd>
            <span>{{ suscripcion.estado }}</span>
          </dd>
          <dt>
            <span>Cliente</span>
          </dt>
          <dd>
            <div v-if="suscripcion.cliente">
              <router-link :to="{ name: 'ClienteView', params: { clienteId: suscripcion.cliente.id } }">{{
                suscripcion.cliente.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Plan</span>
          </dt>
          <dd>
            <div v-if="suscripcion.plan">
              <router-link :to="{ name: 'PlanView', params: { planId: suscripcion.plan.id } }">{{ suscripcion.plan.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link
          v-if="suscripcion.id"
          :to="{ name: 'SuscripcionEdit', params: { suscripcionId: suscripcion.id } }"
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

<script lang="ts" src="./suscripcion-details.component.ts"></script>
