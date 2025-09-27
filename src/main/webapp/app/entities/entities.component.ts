import { defineComponent, provide } from 'vue';

import ClienteService from './cliente/cliente.service';
import TipoProductoService from './tipo-producto/tipo-producto.service';
import ProductoService from './producto/producto.service';
import PlanService from './plan/plan.service';
import SuscripcionService from './suscripcion/suscripcion.service';
import PagoService from './pago/pago.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('clienteService', () => new ClienteService());
    provide('tipoProductoService', () => new TipoProductoService());
    provide('productoService', () => new ProductoService());
    provide('planService', () => new PlanService());
    provide('suscripcionService', () => new SuscripcionService());
    provide('pagoService', () => new PagoService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
