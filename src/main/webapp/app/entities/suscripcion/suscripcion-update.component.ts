import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import SuscripcionService from './suscripcion.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ClienteService from '@/entities/cliente/cliente.service';
import { type ICliente } from '@/shared/model/cliente.model';
import PlanService from '@/entities/plan/plan.service';
import { type IPlan } from '@/shared/model/plan.model';
import { type ISuscripcion, Suscripcion } from '@/shared/model/suscripcion.model';
import { EstadoSuscripcion } from '@/shared/model/enumerations/estado-suscripcion.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SuscripcionUpdate',
  setup() {
    const suscripcionService = inject('suscripcionService', () => new SuscripcionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const suscripcion: Ref<ISuscripcion> = ref(new Suscripcion());

    const clienteService = inject('clienteService', () => new ClienteService());

    const clientes: Ref<ICliente[]> = ref([]);

    const planService = inject('planService', () => new PlanService());

    const plans: Ref<IPlan[]> = ref([]);
    const estadoSuscripcionValues: Ref<string[]> = ref(Object.keys(EstadoSuscripcion));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSuscripcion = async suscripcionId => {
      try {
        const res = await suscripcionService().find(suscripcionId);
        suscripcion.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.suscripcionId) {
      retrieveSuscripcion(route.params.suscripcionId);
    }

    const initRelationships = () => {
      clienteService()
        .retrieve()
        .then(res => {
          clientes.value = res.data;
        });
      planService()
        .retrieve()
        .then(res => {
          plans.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      fechaInicio: {
        required: validations.required('Este campo es obligatorio.'),
      },
      fechaFin: {
        required: validations.required('Este campo es obligatorio.'),
      },
      estado: {
        required: validations.required('Este campo es obligatorio.'),
      },
      cliente: {},
      plan: {},
      pagos: {},
    };
    const v$ = useVuelidate(validationRules, suscripcion as any);
    v$.value.$validate();

    return {
      suscripcionService,
      alertService,
      suscripcion,
      previousState,
      estadoSuscripcionValues,
      isSaving,
      currentLanguage,
      clientes,
      plans,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.suscripcion.id) {
        this.suscripcionService()
          .update(this.suscripcion)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Suscripcion is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.suscripcionService()
          .create(this.suscripcion)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Suscripcion is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
