<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hmvsolucionesSaasMvcApp.tipoProducto.home.createOrEditLabel" data-cy="TipoProductoCreateUpdateHeading">
          Crear o editar Tipo Producto
        </h2>
        <div>
          <div class="form-group" v-if="tipoProducto.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="tipoProducto.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="tipo-producto-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="tipo-producto-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
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
<script lang="ts" src="./tipo-producto-update.component.ts"></script>
