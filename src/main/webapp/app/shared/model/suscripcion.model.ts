import { type ICliente } from '@/shared/model/cliente.model';
import { type IPlan } from '@/shared/model/plan.model';

import { type EstadoSuscripcion } from '@/shared/model/enumerations/estado-suscripcion.model';
export interface ISuscripcion {
  id?: number;
  fechaInicio?: Date;
  fechaFin?: Date;
  estado?: keyof typeof EstadoSuscripcion;
  cliente?: ICliente | null;
  plan?: IPlan | null;
}

export class Suscripcion implements ISuscripcion {
  constructor(
    public id?: number,
    public fechaInicio?: Date,
    public fechaFin?: Date,
    public estado?: keyof typeof EstadoSuscripcion,
    public cliente?: ICliente | null,
    public plan?: IPlan | null,
  ) {}
}
