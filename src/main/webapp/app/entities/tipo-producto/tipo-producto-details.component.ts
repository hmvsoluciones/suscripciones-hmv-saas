import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import TipoProductoService from './tipo-producto.service';
import { type ITipoProducto } from '@/shared/model/tipo-producto.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TipoProductoDetails',
  setup() {
    const tipoProductoService = inject('tipoProductoService', () => new TipoProductoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const tipoProducto: Ref<ITipoProducto> = ref({});

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

    return {
      alertService,
      tipoProducto,

      previousState,
    };
  },
});
