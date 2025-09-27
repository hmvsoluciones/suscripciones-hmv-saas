import { type ITipoProducto } from '@/shared/model/tipo-producto.model';

export interface IProducto {
  id?: number;
  nombre?: string;
  descripcion?: string;
  activo?: boolean | null;
  tipoProducto?: ITipoProducto | null;
}

export class Producto implements IProducto {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string,
    public activo?: boolean | null,
    public tipoProducto?: ITipoProducto | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
