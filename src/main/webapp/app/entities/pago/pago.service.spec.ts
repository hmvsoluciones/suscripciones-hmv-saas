import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import PagoService from './pago.service';
import { DATE_FORMAT } from '@/shared/composables/date-format';
import { Pago } from '@/shared/model/pago.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Pago Service', () => {
    let service: PagoService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PagoService();
      currentDate = new Date();
      elemDefault = new Pago(123, currentDate, 0, 'TARJETA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { fechaPago: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Pago', async () => {
        const returnedFromService = { id: 123, fechaPago: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { fechaPago: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Pago', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Pago', async () => {
        const returnedFromService = {
          fechaPago: dayjs(currentDate).format(DATE_FORMAT),
          monto: 1,
          metodoPago: 'BBBBBB',
          referencia: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { fechaPago: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Pago', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Pago', async () => {
        const patchObject = { fechaPago: dayjs(currentDate).format(DATE_FORMAT), metodoPago: 'BBBBBB', ...new Pago() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fechaPago: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Pago', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Pago', async () => {
        const returnedFromService = {
          fechaPago: dayjs(currentDate).format(DATE_FORMAT),
          monto: 1,
          metodoPago: 'BBBBBB',
          referencia: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { fechaPago: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Pago', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Pago', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Pago', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
