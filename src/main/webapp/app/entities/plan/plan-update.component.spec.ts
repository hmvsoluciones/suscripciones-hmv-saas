import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PlanUpdate from './plan-update.vue';
import PlanService from './plan.service';
import AlertService from '@/shared/alert/alert.service';

import ProductoService from '@/entities/producto/producto.service';

type PlanUpdateComponentType = InstanceType<typeof PlanUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const planSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PlanUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Plan Management Update Component', () => {
    let comp: PlanUpdateComponentType;
    let planServiceStub: SinonStubbedInstance<PlanService>;

    beforeEach(() => {
      route = {};
      planServiceStub = sinon.createStubInstance<PlanService>(PlanService);
      planServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          planService: () => planServiceStub,
          productoService: () =>
            sinon.createStubInstance<ProductoService>(ProductoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(PlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.plan = planSample;
        planServiceStub.update.resolves(planSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(planServiceStub.update.calledWith(planSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        planServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.plan = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(planServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        planServiceStub.find.resolves(planSample);
        planServiceStub.retrieve.resolves([planSample]);

        // WHEN
        route = {
          params: {
            planId: `${planSample.id}`,
          },
        };
        const wrapper = shallowMount(PlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.plan).toMatchObject(planSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        planServiceStub.find.resolves(planSample);
        const wrapper = shallowMount(PlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
