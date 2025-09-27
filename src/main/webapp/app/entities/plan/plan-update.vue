<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hmvsolucionesSaasMvcApp.plan.home.createOrEditLabel" data-cy="PlanCreateUpdateHeading">Crear o editar Plan</h2>
        <div>
          <div class="form-group" v-if="plan.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="plan.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="plan-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="plan-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="plan-descripcion">Descripcion</label>
            <input
              type="text"
              class="form-control"
              name="descripcion"
              id="plan-descripcion"
              data-cy="descripcion"
              :class="{ valid: !v$.descripcion.$invalid, invalid: v$.descripcion.$invalid }"
              v-model="v$.descripcion.$model"
              required
            />
            <div v-if="v$.descripcion.$anyDirty && v$.descripcion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.descripcion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="plan-precio">Precio</label>
            <input
              type="number"
              class="form-control"
              name="precio"
              id="plan-precio"
              data-cy="precio"
              :class="{ valid: !v$.precio.$invalid, invalid: v$.precio.$invalid }"
              v-model.number="v$.precio.$model"
              required
            />
            <div v-if="v$.precio.$anyDirty && v$.precio.$invalid">
              <small class="form-text text-danger" v-for="error of v$.precio.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="plan-duracionMeses">Duracion Meses</label>
            <input
              type="number"
              class="form-control"
              name="duracionMeses"
              id="plan-duracionMeses"
              data-cy="duracionMeses"
              :class="{ valid: !v$.duracionMeses.$invalid, invalid: v$.duracionMeses.$invalid }"
              v-model.number="v$.duracionMeses.$model"
              required
            />
            <div v-if="v$.duracionMeses.$anyDirty && v$.duracionMeses.$invalid">
              <small class="form-text text-danger" v-for="error of v$.duracionMeses.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="plan-tipoPago">Tipo Pago</label>
            <select
              class="form-control"
              name="tipoPago"
              :class="{ valid: !v$.tipoPago.$invalid, invalid: v$.tipoPago.$invalid }"
              v-model="v$.tipoPago.$model"
              id="plan-tipoPago"
              data-cy="tipoPago"
              required
            >
              <option v-for="tipoPagoPlan in tipoPagoPlanValues" :key="tipoPagoPlan" :value="tipoPagoPlan">{{ tipoPagoPlan }}</option>
            </select>
            <div v-if="v$.tipoPago.$anyDirty && v$.tipoPago.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tipoPago.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="plan-producto">Producto</label>
            <select class="form-control" id="plan-producto" data-cy="producto" name="producto" v-model="plan.producto">
              <option :value="null"></option>
              <option
                :value="plan.producto && productoOption.id === plan.producto.id ? plan.producto : productoOption"
                v-for="productoOption in productos"
                :key="productoOption.id"
              >
                {{ productoOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancelar</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Guardar</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./plan-update.component.ts"></script>
