import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PagoUpdate from './pago-update.vue';
import PagoService from './pago.service';
import AlertService from '@/shared/alert/alert.service';

import SuscripcionService from '@/entities/suscripcion/suscripcion.service';

type PagoUpdateComponentType = InstanceType<typeof PagoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const pagoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PagoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Pago Management Update Component', () => {
    let comp: PagoUpdateComponentType;
    let pagoServiceStub: SinonStubbedInstance<PagoService>;

    beforeEach(() => {
      route = {};
      pagoServiceStub = sinon.createStubInstance<PagoService>(PagoService);
      pagoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          pagoService: () => pagoServiceStub,
          suscripcionService: () =>
            sinon.createStubInstance<SuscripcionService>(SuscripcionService, {
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
        const wrapper = shallowMount(PagoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.pago = pagoSample;
        pagoServiceStub.update.resolves(pagoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pagoServiceStub.update.calledWith(pagoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        pagoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PagoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.pago = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pagoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        pagoServiceStub.find.resolves(pagoSample);
        pagoServiceStub.retrieve.resolves([pagoSample]);

        // WHEN
        route = {
          params: {
            pagoId: `${pagoSample.id}`,
          },
        };
        const wrapper = shallowMount(PagoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.pago).toMatchObject(pagoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        pagoServiceStub.find.resolves(pagoSample);
        const wrapper = shallowMount(PagoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
