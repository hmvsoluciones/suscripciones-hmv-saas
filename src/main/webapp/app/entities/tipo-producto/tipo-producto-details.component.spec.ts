import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TipoProductoDetails from './tipo-producto-details.vue';
import TipoProductoService from './tipo-producto.service';
import AlertService from '@/shared/alert/alert.service';

type TipoProductoDetailsComponentType = InstanceType<typeof TipoProductoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tipoProductoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('TipoProducto Management Detail Component', () => {
    let tipoProductoServiceStub: SinonStubbedInstance<TipoProductoService>;
    let mountOptions: MountingOptions<TipoProductoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      tipoProductoServiceStub = sinon.createStubInstance<TipoProductoService>(TipoProductoService);

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          tipoProductoService: () => tipoProductoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        tipoProductoServiceStub.find.resolves(tipoProductoSample);
        route = {
          params: {
            tipoProductoId: `${123}`,
          },
        };
        const wrapper = shallowMount(TipoProductoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.tipoProducto).toMatchObject(tipoProductoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tipoProductoServiceStub.find.resolves(tipoProductoSample);
        const wrapper = shallowMount(TipoProductoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
