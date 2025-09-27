import { Authority } from '@/shared/security/authority';
const Entities = () => import('@/entities/entities.vue');

const Cliente = () => import('@/entities/cliente/cliente.vue');
const ClienteUpdate = () => import('@/entities/cliente/cliente-update.vue');
const ClienteDetails = () => import('@/entities/cliente/cliente-details.vue');

const TipoProducto = () => import('@/entities/tipo-producto/tipo-producto.vue');
const TipoProductoUpdate = () => import('@/entities/tipo-producto/tipo-producto-update.vue');
const TipoProductoDetails = () => import('@/entities/tipo-producto/tipo-producto-details.vue');

const Producto = () => import('@/entities/producto/producto.vue');
const ProductoUpdate = () => import('@/entities/producto/producto-update.vue');
const ProductoDetails = () => import('@/entities/producto/producto-details.vue');

const Plan = () => import('@/entities/plan/plan.vue');
const PlanUpdate = () => import('@/entities/plan/plan-update.vue');
const PlanDetails = () => import('@/entities/plan/plan-details.vue');

const Suscripcion = () => import('@/entities/suscripcion/suscripcion.vue');
const SuscripcionUpdate = () => import('@/entities/suscripcion/suscripcion-update.vue');
const SuscripcionDetails = () => import('@/entities/suscripcion/suscripcion-details.vue');

const Pago = () => import('@/entities/pago/pago.vue');
const PagoUpdate = () => import('@/entities/pago/pago-update.vue');
const PagoDetails = () => import('@/entities/pago/pago-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'cliente',
      name: 'Cliente',
      component: Cliente,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cliente/new',
      name: 'ClienteCreate',
      component: ClienteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cliente/:clienteId/edit',
      name: 'ClienteEdit',
      component: ClienteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cliente/:clienteId/view',
      name: 'ClienteView',
      component: ClienteDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-producto',
      name: 'TipoProducto',
      component: TipoProducto,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-producto/new',
      name: 'TipoProductoCreate',
      component: TipoProductoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-producto/:tipoProductoId/edit',
      name: 'TipoProductoEdit',
      component: TipoProductoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-producto/:tipoProductoId/view',
      name: 'TipoProductoView',
      component: TipoProductoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'producto',
      name: 'Producto',
      component: Producto,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'producto/new',
      name: 'ProductoCreate',
      component: ProductoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'producto/:productoId/edit',
      name: 'ProductoEdit',
      component: ProductoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'producto/:productoId/view',
      name: 'ProductoView',
      component: ProductoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plan',
      name: 'Plan',
      component: Plan,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plan/new',
      name: 'PlanCreate',
      component: PlanUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plan/:planId/edit',
      name: 'PlanEdit',
      component: PlanUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plan/:planId/view',
      name: 'PlanView',
      component: PlanDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'suscripcion',
      name: 'Suscripcion',
      component: Suscripcion,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'suscripcion/new',
      name: 'SuscripcionCreate',
      component: SuscripcionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'suscripcion/:suscripcionId/edit',
      name: 'SuscripcionEdit',
      component: SuscripcionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'suscripcion/:suscripcionId/view',
      name: 'SuscripcionView',
      component: SuscripcionDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pago',
      name: 'Pago',
      component: Pago,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pago/new',
      name: 'PagoCreate',
      component: PagoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pago/:pagoId/edit',
      name: 'PagoEdit',
      component: PagoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pago/:pagoId/view',
      name: 'PagoView',
      component: PagoDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
