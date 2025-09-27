import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PlanService from './plan.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ProductoService from '@/entities/producto/producto.service';
import { type IProducto } from '@/shared/model/producto.model';
import { type IPlan, Plan } from '@/shared/model/plan.model';
import { TipoPagoPlan } from '@/shared/model/enumerations/tipo-pago-plan.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PlanUpdate',
  setup() {
    const planService = inject('planService', () => new PlanService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const plan: Ref<IPlan> = ref(new Plan());

    const productoService = inject('productoService', () => new ProductoService());

    const productos: Ref<IProducto[]> = ref([]);
    const tipoPagoPlanValues: Ref<string[]> = ref(Object.keys(TipoPagoPlan));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePlan = async planId => {
      try {
        const res = await planService().find(planId);
        plan.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.planId) {
      retrievePlan(route.params.planId);
    }

    const initRelationships = () => {
      productoService()
        .retrieve()
        .then(res => {
          productos.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 150 caracteres.', 150),
      },
      descripcion: {
        required: validations.required('Este campo es obligatorio.'),
        maxLength: validations.maxLength('Este campo no puede superar más de 500 caracteres.', 500),
      },
      precio: {
        required: validations.required('Este campo es obligatorio.'),
        min: validations.minValue('Este campo debe ser mayor que 0.', 0),
      },
      duracionMeses: {
        required: validations.required('Este campo es obligatorio.'),
        integer: validations.integer('Este campo debe ser un número.'),
        min: validations.minValue('Este campo debe ser mayor que 1.', 1),
      },
      tipoPago: {
        required: validations.required('Este campo es obligatorio.'),
      },
      producto: {},
      suscripcions: {},
    };
    const v$ = useVuelidate(validationRules, plan as any);
    v$.value.$validate();

    return {
      planService,
      alertService,
      plan,
      previousState,
      tipoPagoPlanValues,
      isSaving,
      currentLanguage,
      productos,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.plan.id) {
        this.planService()
          .update(this.plan)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Plan is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.planService()
          .create(this.plan)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Plan is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
