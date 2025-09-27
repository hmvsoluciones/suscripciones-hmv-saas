import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Suscripcion from './suscripcion.vue';
import SuscripcionService from './suscripcion.service';
import AlertService from '@/shared/alert/alert.service';

type SuscripcionComponentType = InstanceType<typeof Suscripcion>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Suscripcion Management Component', () => {
    let suscripcionServiceStub: SinonStubbedInstance<SuscripcionService>;
    let mountOptions: MountingOptions<SuscripcionComponentType>['global'];

    beforeEach(() => {
      suscripcionServiceStub = sinon.createStubInstance<SuscripcionService>(SuscripcionService);
      suscripcionServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          suscripcionService: () => suscripcionServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        suscripcionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Suscripcion, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(suscripcionServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.suscripcions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(Suscripcion, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(suscripcionServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: SuscripcionComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Suscripcion, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        suscripcionServiceStub.retrieve.reset();
        suscripcionServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        suscripcionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(suscripcionServiceStub.retrieve.called).toBeTruthy();
        expect(comp.suscripcions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(suscripcionServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        suscripcionServiceStub.retrieve.reset();
        suscripcionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(suscripcionServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.suscripcions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(suscripcionServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        suscripcionServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeSuscripcion();
        await comp.$nextTick(); // clear components

        // THEN
        expect(suscripcionServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(suscripcionServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
