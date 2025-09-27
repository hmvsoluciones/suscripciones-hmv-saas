import { type ISuscripcion } from '@/shared/model/suscripcion.model';

import { type MetodoPago } from '@/shared/model/enumerations/metodo-pago.model';
export interface IPago {
  id?: number;
  fechaPago?: Date;
  monto?: number;
  metodoPago?: keyof typeof MetodoPago;
  referencia?: string | null;
  suscripcion?: ISuscripcion | null;
}

export class Pago implements IPago {
  constructor(
    public id?: number,
    public fechaPago?: Date,
    public monto?: number,
    public metodoPago?: keyof typeof MetodoPago,
    public referencia?: string | null,
    public suscripcion?: ISuscripcion | null,
  ) {}
}
