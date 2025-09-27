<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hmvsolucionesSaasMvcApp.producto.home.createOrEditLabel" data-cy="ProductoCreateUpdateHeading">Crear o editar Producto</h2>
        <div>
          <div class="form-group" v-if="producto.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="producto.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="producto-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="producto-nombre"
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
            <label class="form-control-label" for="producto-descripcion">Descripcion</label>
            <input
              type="text"
              class="form-control"
              name="descripcion"
              id="producto-descripcion"
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
            <label class="form-control-label" for="producto-activo">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="producto-activo"
              data-cy="activo"
              :class="{ valid: !v$.activo.$invalid, invalid: v$.activo.$invalid }"
              v-model="v$.activo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="producto-tipoProducto">Tipo Producto</label>
            <select
              class="form-control"
              id="producto-tipoProducto"
              data-cy="tipoProducto"
              name="tipoProducto"
              v-model="producto.tipoProducto"
            >
              <option :value="null"></option>
              <option
                :value="
                  producto.tipoProducto && tipoProductoOption.id === producto.tipoProducto.id ? producto.tipoProducto : tipoProductoOption
                "
                v-for="tipoProductoOption in tipoProductos"
                :key="tipoProductoOption.id"
              >
                {{ tipoProductoOption.nombre }}
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
<script lang="ts" src="./producto-update.component.ts"></script>
