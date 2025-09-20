<template>
  <b-sidebar
    id="sidebar-drawer"
    :visible="visible"
    @hidden="$emit('update:visible', false)"
    shadow
    backdrop
    :right="false"
    width="280px"
    class="bg-white sidebar-custom"
  >
    <!-- Título con logo opcional -->
    <template #title>
      <div class="sidebar-header d-flex align-items-center justify-content-between">
        <strong>{{ companyName }}</strong>
        <b-button size="sm" variant="outline-secondary" @click="$emit('update:visible', false)">
          <font-awesome-icon icon="x" />
        </b-button>
      </div>
    </template>

    <!-- Menú principal -->
    <b-nav vertical class="flex-column mt-3 sidebar-nav">
      <!-- Inicio -->
      <b-nav-item to="/" exact class="d-flex align-items-center"> <font-awesome-icon icon="home" class="me-2" /> Inicio </b-nav-item>

      <!-- Sección Admin -->
      <div v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated" class="sidebar-section mt-3">
        <small class="text-muted px-3">Administración</small>
        <b-nav-item to="/admin" class="d-flex align-items-center">
          <font-awesome-icon icon="user-shield" class="me-2" /> Panel Admin
        </b-nav-item>
      </div>

      <!-- Sección Usuario -->
      <div v-if="hasAnyAuthority('ROLE_USER') && authenticated" class="sidebar-section mt-3">
        <small class="text-muted px-3">Usuario</small>
        <b-nav-item to="/user" class="d-flex align-items-center"> <font-awesome-icon icon="user" class="me-2" /> Panel Usuario </b-nav-item>
      </div>
    </b-nav>

    <!-- Pie de sidebar -->
    <div class="sidebar-footer mt-auto px-3 py-2 text-center">
      <small class="text-muted">&copy; HMV Soluciones</small>
    </div>
  </b-sidebar>
</template>

<script lang="ts" src="./sidebar.component.ts"></script>

<style scoped>
.sidebar-custom {
  border-right: 1px solid #dee2e6;
}

.sidebar-header {
  padding: 0.75rem 1rem;
  border-bottom: 1px solid #dee2e6;
  font-size: 1.1rem;
}

.sidebar-nav b-nav-item {
  padding: 0.5rem 1rem;
  font-weight: 500;
  transition: background 0.2s;
}

.sidebar-nav b-nav-item:hover {
  background-color: #f8f9fa;
  border-radius: 0.25rem;
}

.sidebar-section small {
  display: block;
  margin-bottom: 0.25rem;
  font-size: 0.75rem;
  text-transform: uppercase;
}

.sidebar-footer {
  font-size: 0.75rem;
  color: #6c757d;
}
</style>
