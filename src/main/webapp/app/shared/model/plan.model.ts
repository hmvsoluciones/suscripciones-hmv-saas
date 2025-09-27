import { type IProducto } from '@/shared/model/producto.model';

import { type TipoPagoPlan } from '@/shared/model/enumerations/tipo-pago-plan.model';
export interface IPlan {
  id?: number;
  nombre?: string;
  descripcion?: string;
  precio?: number;
  duracionMeses?: number;
  tipoPago?: keyof typeof TipoPagoPlan;
  producto?: IProducto | null;
}

export class Plan implements IPlan {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string,
    public precio?: number,
    public duracionMeses?: number,
    public tipoPago?: keyof typeof TipoPagoPlan,
    public producto?: IProducto | null,
  ) {}
}
