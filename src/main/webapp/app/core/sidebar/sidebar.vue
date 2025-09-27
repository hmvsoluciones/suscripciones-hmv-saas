<template>
  <div>
    <!-- Overlay para fondo oscuro -->
    <div v-if="visible" class="sidebar-overlay" @click="closeSidebar"></div>

    <!-- Sidebar -->
    <div class="sidebar" :class="{ 'sidebar-open': visible }">
      <!-- Header -->
      <div class="sidebar-header">
        <div class="d-flex align-items-center justify-content-between p-3 border-bottom">
          <strong class="sidebar-title">Mi Aplicación</strong>
          <button class="btn btn-outline-secondary btn-sm sidebar-close-btn" @click="closeSidebar">
            <font-awesome-icon icon="times" />
          </button>
        </div>
      </div>

      <!-- Contenido -->
      <div class="sidebar-content">
        <nav class="sidebar-nav">
          <div class="nav flex-column">
            <!-- Grupo: Navegación Principal -->
            <div class="sidebar-group">
              <div class="sidebar-group-header">
                <small class="sidebar-group-title">Navegación</small>
              </div>
              <router-link to="/" class="nav-link sidebar-nav-item" @click="closeSidebar" exact>
                <font-awesome-icon icon="home" class="sidebar-icon" />
                <span class="sidebar-text">Inicio</span>
              </router-link>
            </div>

            <!-- Grupo: Autenticación -->
            <div class="sidebar-group">
              <div class="sidebar-group-header">
                <small class="sidebar-group-title">Cuenta</small>
              </div>
              <a href="#" class="nav-link sidebar-nav-item" @click="handleLoginClick">
                <font-awesome-icon icon="sign-in-alt" class="sidebar-icon" />
                <span class="sidebar-text">Iniciar sesión</span>
              </a>
              <router-link to="/register" class="nav-link sidebar-nav-item" @click="closeSidebar">
                <font-awesome-icon icon="user-plus" class="sidebar-icon" />
                <span class="sidebar-text">Registrarse</span>
              </router-link>
            </div>

            <!-- Grupo: Administración (ejemplo condicional) -->
            <div class="sidebar-group" v-if="showAdminSection">
              <div class="sidebar-group-header">
                <small class="sidebar-group-title">Administración</small>
              </div>
              <router-link to="/admin" class="nav-link sidebar-nav-item" @click="closeSidebar">
                <font-awesome-icon icon="users" class="sidebar-icon" />
                <span class="sidebar-text">Panel Admin</span>
              </router-link>

              <router-link to="/admin/users" class="nav-link sidebar-nav-item" @click="closeSidebar">
                <font-awesome-icon icon="user-cog" class="sidebar-icon" />
                <span class="sidebar-text">Gestión Usuarios</span>
              </router-link>
            </div>

            <!-- Grupo: Configuración (ejemplo condicional) -->
            <div class="sidebar-group" v-if="isAuthenticated">
              <div class="sidebar-group-header">
                <small class="sidebar-group-title">Mi Cuenta</small>
              </div>
              <router-link to="/profile" class="nav-link sidebar-nav-item" @click="closeSidebar">
                <font-awesome-icon icon="user" class="sidebar-icon" />
                <span class="sidebar-text">Perfil</span>
              </router-link>

              <router-link to="/settings" class="nav-link sidebar-nav-item" @click="closeSidebar">
                <font-awesome-icon icon="cogs" class="sidebar-icon" />
                <span class="sidebar-text">Configuración</span>
              </router-link>

              <a href="#" class="nav-link sidebar-nav-item" @click="handleLogout">
                <font-awesome-icon icon="sign-out-alt" class="sidebar-icon" />
                <span class="sidebar-text">Cerrar Sesión</span>
              </a>
            </div>
          </div>
        </nav>
      </div>

      <!-- Footer -->
      <div class="sidebar-footer p-3 border-top">
        <small class="text-muted d-block text-center"> &copy; {{ new Date().getFullYear() }} Mi Aplicación </small>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./sidebar.component.ts"></script>

<style scoped lang="scss" src="./sidebar.component.scss"></style>
