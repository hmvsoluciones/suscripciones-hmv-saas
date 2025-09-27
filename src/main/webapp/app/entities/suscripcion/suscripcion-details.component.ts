import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import SuscripcionService from './suscripcion.service';
import { type ISuscripcion } from '@/shared/model/suscripcion.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SuscripcionDetails',
  setup() {
    const suscripcionService = inject('suscripcionService', () => new SuscripcionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const suscripcion: Ref<ISuscripcion> = ref({});

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

    return {
      alertService,
      suscripcion,

      previousState,
    };
  },
});
