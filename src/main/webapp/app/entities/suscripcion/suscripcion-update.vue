<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hmvsolucionesSaasMvcApp.suscripcion.home.createOrEditLabel" data-cy="SuscripcionCreateUpdateHeading">
          Crear o editar Suscripcion
        </h2>
        <div>
          <div class="form-group" v-if="suscripcion.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="suscripcion.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="suscripcion-fechaInicio">Fecha Inicio</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="suscripcion-fechaInicio"
                  v-model="v$.fechaInicio.$model"
                  name="fechaInicio"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="suscripcion-fechaInicio"
                data-cy="fechaInicio"
                type="text"
                class="form-control"
                name="fechaInicio"
                :class="{ valid: !v$.fechaInicio.$invalid, invalid: v$.fechaInicio.$invalid }"
                v-model="v$.fechaInicio.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaInicio.$anyDirty && v$.fechaInicio.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaInicio.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="suscripcion-fechaFin">Fecha Fin</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="suscripcion-fechaFin"
                  v-model="v$.fechaFin.$model"
                  name="fechaFin"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="suscripcion-fechaFin"
                data-cy="fechaFin"
                type="text"
                class="form-control"
                name="fechaFin"
                :class="{ valid: !v$.fechaFin.$invalid, invalid: v$.fechaFin.$invalid }"
                v-model="v$.fechaFin.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaFin.$anyDirty && v$.fechaFin.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaFin.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="suscripcion-estado">Estado</label>
            <select
              class="form-control"
              name="estado"
              :class="{ valid: !v$.estado.$invalid, invalid: v$.estado.$invalid }"
              v-model="v$.estado.$model"
              id="suscripcion-estado"
              data-cy="estado"
              required
            >
              <option v-for="estadoSuscripcion in estadoSuscripcionValues" :key="estadoSuscripcion" :value="estadoSuscripcion">
                {{ estadoSuscripcion }}
              </option>
            </select>
            <div v-if="v$.estado.$anyDirty && v$.estado.$invalid">
              <small class="form-text text-danger" v-for="error of v$.estado.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="suscripcion-cliente">Cliente</label>
            <select class="form-control" id="suscripcion-cliente" data-cy="cliente" name="cliente" v-model="suscripcion.cliente">
              <option :value="null"></option>
              <option
                :value="suscripcion.cliente && clienteOption.id === suscripcion.cliente.id ? suscripcion.cliente : clienteOption"
                v-for="clienteOption in clientes"
                :key="clienteOption.id"
              >
                {{ clienteOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="suscripcion-plan">Plan</label>
            <select class="form-control" id="suscripcion-plan" data-cy="plan" name="plan" v-model="suscripcion.plan">
              <option :value="null"></option>
              <option
                :value="suscripcion.plan && planOption.id === suscripcion.plan.id ? suscripcion.plan : planOption"
                v-for="planOption in plans"
                :key="planOption.id"
              >
                {{ planOption.id }}
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
<script lang="ts" src="./suscripcion-update.component.ts"></script>
