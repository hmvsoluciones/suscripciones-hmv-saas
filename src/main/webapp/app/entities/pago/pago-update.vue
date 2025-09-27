<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hmvsolucionesSaasMvcApp.pago.home.createOrEditLabel" data-cy="PagoCreateUpdateHeading">Crear o editar Pago</h2>
        <div>
          <div class="form-group" v-if="pago.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="pago.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="pago-fechaPago">Fecha Pago</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="pago-fechaPago"
                  v-model="v$.fechaPago.$model"
                  name="fechaPago"
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
                id="pago-fechaPago"
                data-cy="fechaPago"
                type="text"
                class="form-control"
                name="fechaPago"
                :class="{ valid: !v$.fechaPago.$invalid, invalid: v$.fechaPago.$invalid }"
                v-model="v$.fechaPago.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaPago.$anyDirty && v$.fechaPago.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaPago.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="pago-monto">Monto</label>
            <input
              type="number"
              class="form-control"
              name="monto"
              id="pago-monto"
              data-cy="monto"
              :class="{ valid: !v$.monto.$invalid, invalid: v$.monto.$invalid }"
              v-model.number="v$.monto.$model"
              required
            />
            <div v-if="v$.monto.$anyDirty && v$.monto.$invalid">
              <small class="form-text text-danger" v-for="error of v$.monto.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="pago-metodoPago">Metodo Pago</label>
            <select
              class="form-control"
              name="metodoPago"
              :class="{ valid: !v$.metodoPago.$invalid, invalid: v$.metodoPago.$invalid }"
              v-model="v$.metodoPago.$model"
              id="pago-metodoPago"
              data-cy="metodoPago"
              required
            >
              <option v-for="metodoPago in metodoPagoValues" :key="metodoPago" :value="metodoPago">{{ metodoPago }}</option>
            </select>
            <div v-if="v$.metodoPago.$anyDirty && v$.metodoPago.$invalid">
              <small class="form-text text-danger" v-for="error of v$.metodoPago.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="pago-referencia">Referencia</label>
            <input
              type="text"
              class="form-control"
              name="referencia"
              id="pago-referencia"
              data-cy="referencia"
              :class="{ valid: !v$.referencia.$invalid, invalid: v$.referencia.$invalid }"
              v-model="v$.referencia.$model"
            />
            <div v-if="v$.referencia.$anyDirty && v$.referencia.$invalid">
              <small class="form-text text-danger" v-for="error of v$.referencia.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="pago-suscripcion">Suscripcion</label>
            <select class="form-control" id="pago-suscripcion" data-cy="suscripcion" name="suscripcion" v-model="pago.suscripcion">
              <option :value="null"></option>
              <option
                :value="pago.suscripcion && suscripcionOption.id === pago.suscripcion.id ? pago.suscripcion : suscripcionOption"
                v-for="suscripcionOption in suscripcions"
                :key="suscripcionOption.id"
              >
                {{ pago.suscripcion.id }} - {{ pago.suscripcion.cliente.nombre }} - {{ pago.suscripcion.plan.nombre }}
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
<script lang="ts" src="./pago-update.component.ts"></script>
