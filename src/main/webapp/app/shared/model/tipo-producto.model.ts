export interface ITipoProducto {
  id?: number;
  nombre?: string;
}

export class TipoProducto implements ITipoProducto {
  constructor(
    public id?: number,
    public nombre?: string,
  ) {}
}
