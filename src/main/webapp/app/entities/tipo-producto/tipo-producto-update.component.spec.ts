import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TipoProductoUpdate from './tipo-producto-update.vue';
import TipoProductoService from './tipo-producto.service';
import AlertService from '@/shared/alert/alert.service';

type TipoProductoUpdateComponentType = InstanceType<typeof TipoProductoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tipoProductoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TipoProductoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('TipoProducto Management Update Component', () => {
    let comp: TipoProductoUpdateComponentType;
    let tipoProductoServiceStub: SinonStubbedInstance<TipoProductoService>;

    beforeEach(() => {
      route = {};
      tipoProductoServiceStub = sinon.createStubInstance<TipoProductoService>(TipoProductoService);
      tipoProductoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          tipoProductoService: () => tipoProductoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(TipoProductoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tipoProducto = tipoProductoSample;
        tipoProductoServiceStub.update.resolves(tipoProductoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tipoProductoServiceStub.update.calledWith(tipoProductoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        tipoProductoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TipoProductoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tipoProducto = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tipoProductoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        tipoProductoServiceStub.find.resolves(tipoProductoSample);
        tipoProductoServiceStub.retrieve.resolves([tipoProductoSample]);

        // WHEN
        route = {
          params: {
            tipoProductoId: `${tipoProductoSample.id}`,
          },
        };
        const wrapper = shallowMount(TipoProductoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.tipoProducto).toMatchObject(tipoProductoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tipoProductoServiceStub.find.resolves(tipoProductoSample);
        const wrapper = shallowMount(TipoProductoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
