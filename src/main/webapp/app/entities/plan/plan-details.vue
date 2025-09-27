<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="plan">
        <h2 class="jh-entity-heading" data-cy="planDetailsHeading"><span>Plan</span> {{ plan.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nombre</span>
          </dt>
          <dd>
            <span>{{ plan.nombre }}</span>
          </dd>
          <dt>
            <span>Descripcion</span>
          </dt>
          <dd>
            <span>{{ plan.descripcion }}</span>
          </dd>
          <dt>
            <span>Precio</span>
          </dt>
          <dd>
            <span>{{ plan.precio }}</span>
          </dd>
          <dt>
            <span>Duracion Meses</span>
          </dt>
          <dd>
            <span>{{ plan.duracionMeses }}</span>
          </dd>
          <dt>
            <span>Tipo Pago</span>
          </dt>
          <dd>
            <span>{{ plan.tipoPago }}</span>
          </dd>
          <dt>
            <span>Producto</span>
          </dt>
          <dd>
            <div v-if="plan.producto">
              <router-link :to="{ name: 'ProductoView', params: { productoId: plan.producto.id } }">{{ plan.producto.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
        </button>
        <router-link v-if="plan.id" :to="{ name: 'PlanEdit', params: { planId: plan.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./plan-details.component.ts"></script>
