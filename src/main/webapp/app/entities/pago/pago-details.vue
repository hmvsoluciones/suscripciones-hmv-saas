<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="pago">
        <h2 class="jh-entity-heading" data-cy="pagoDetailsHeading"><span>Pago</span> {{ pago.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Fecha Pago</span>
          </dt>
          <dd>
            <span>{{ pago.fechaPago }}</span>
          </dd>
          <dt>
            <span>Monto</span>
          </dt>
          <dd>
            <span>{{ pago.monto }}</span>
          </dd>
          <dt>
            <span>Metodo Pago</span>
          </dt>
          <dd>
            <span>{{ pago.metodoPago }}</span>
          </dd>
          <dt>
            <span>Referencia</span>
          </dt>
          <dd>
            <span>{{ pago.referencia }}</span>
          </dd>
          <dt>
            <span>Suscripcion</span>
          </dt>
          <dd>
            <div v-if="pago.suscripcion">
              <router-link :to="{ name: 'SuscripcionView', params: { suscripcionId: pago.suscripcion.id } }">{{
                pago.suscripcion.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="pago.id" :to="{ name: 'PagoEdit', params: { pagoId: pago.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./pago-details.component.ts"></script>
