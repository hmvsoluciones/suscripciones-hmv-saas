import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import TipoProductoService from './tipo-producto.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type ITipoProducto, TipoProducto } from '@/shared/model/tipo-producto.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TipoProductoUpdate',
  setup() {
    const tipoProductoService = inject('tipoProductoService', () => new TipoProductoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const tipoProducto: Ref<ITipoProducto> = ref(new TipoProducto());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTipoProducto = async tipoProductoId => {
      try {
        const res = await tipoProductoService().find(tipoProductoId);
        tipoProducto.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tipoProductoId) {
      retrieveTipoProducto(route.params.tipoProductoId);
    }

    const initRelationships = () => {};

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 100 caracteres.', 100),
      },
      productos: {},
    };
    const v$ = useVuelidate(validationRules, tipoProducto as any);
    v$.value.$validate();

    return {
      tipoProductoService,
      alertService,
      tipoProducto,
      previousState,
      isSaving,
      currentLanguage,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.tipoProducto.id) {
        this.tipoProductoService()
          .update(this.tipoProducto)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A TipoProducto is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.tipoProductoService()
          .create(this.tipoProducto)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A TipoProducto is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
