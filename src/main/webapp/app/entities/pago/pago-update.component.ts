import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PagoService from './pago.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SuscripcionService from '@/entities/suscripcion/suscripcion.service';
import { type ISuscripcion } from '@/shared/model/suscripcion.model';
import { type IPago, Pago } from '@/shared/model/pago.model';
import { MetodoPago } from '@/shared/model/enumerations/metodo-pago.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PagoUpdate',
  setup() {
    const pagoService = inject('pagoService', () => new PagoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const pago: Ref<IPago> = ref(new Pago());

    const suscripcionService = inject('suscripcionService', () => new SuscripcionService());

    const suscripcions: Ref<ISuscripcion[]> = ref([]);
    const metodoPagoValues: Ref<string[]> = ref(Object.keys(MetodoPago));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePago = async pagoId => {
      try {
        const res = await pagoService().find(pagoId);
        pago.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.pagoId) {
      retrievePago(route.params.pagoId);
    }

    const initRelationships = () => {
      suscripcionService()
        .retrieve()
        .then(res => {
          suscripcions.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      fechaPago: {
        required: validations.required('Este campo es obligatorio.'),
      },
      monto: {
        required: validations.required('Este campo es obligatorio.'),
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
      },
      metodoPago: {
        required: validations.required('Este campo es obligatorio.'),
      },
      referencia: {
        maxLength: validations.maxLength('Este campo no puede superar más de 200 caracteres.', 200),
      },
      suscripcion: {},
    };
    const v$ = useVuelidate(validationRules, pago as any);
    v$.value.$validate();

    return {
      pagoService,
      alertService,
      pago,
      previousState,
      metodoPagoValues,
      isSaving,
      currentLanguage,
      suscripcions,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.pago.id) {
        this.pagoService()
          .update(this.pago)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Pago is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.pagoService()
          .create(this.pago)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Pago is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
