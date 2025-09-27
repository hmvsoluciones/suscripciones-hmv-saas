import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PlanDetails from './plan-details.vue';
import PlanService from './plan.service';
import AlertService from '@/shared/alert/alert.service';

type PlanDetailsComponentType = InstanceType<typeof PlanDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const planSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Plan Management Detail Component', () => {
    let planServiceStub: SinonStubbedInstance<PlanService>;
    let mountOptions: MountingOptions<PlanDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      planServiceStub = sinon.createStubInstance<PlanService>(PlanService);

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
          planService: () => planServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        planServiceStub.find.resolves(planSample);
        route = {
          params: {
            planId: `${123}`,
          },
        };
        const wrapper = shallowMount(PlanDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.plan).toMatchObject(planSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        planServiceStub.find.resolves(planSample);
        const wrapper = shallowMount(PlanDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
