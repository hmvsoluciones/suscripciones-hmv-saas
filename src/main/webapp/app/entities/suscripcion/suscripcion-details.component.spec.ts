import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SuscripcionDetails from './suscripcion-details.vue';
import SuscripcionService from './suscripcion.service';
import AlertService from '@/shared/alert/alert.service';

type SuscripcionDetailsComponentType = InstanceType<typeof SuscripcionDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const suscripcionSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Suscripcion Management Detail Component', () => {
    let suscripcionServiceStub: SinonStubbedInstance<SuscripcionService>;
    let mountOptions: MountingOptions<SuscripcionDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      suscripcionServiceStub = sinon.createStubInstance<SuscripcionService>(SuscripcionService);

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
          suscripcionService: () => suscripcionServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        suscripcionServiceStub.find.resolves(suscripcionSample);
        route = {
          params: {
            suscripcionId: `${123}`,
          },
        };
        const wrapper = shallowMount(SuscripcionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.suscripcion).toMatchObject(suscripcionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        suscripcionServiceStub.find.resolves(suscripcionSample);
        const wrapper = shallowMount(SuscripcionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
