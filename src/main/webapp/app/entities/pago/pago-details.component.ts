import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import PagoService from './pago.service';
import { type IPago } from '@/shared/model/pago.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PagoDetails',
  setup() {
    const pagoService = inject('pagoService', () => new PagoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const pago: Ref<IPago> = ref({});

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

    return {
      alertService,
      pago,

      previousState,
    };
  },
});
