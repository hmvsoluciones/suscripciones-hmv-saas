import { defineComponent, computed } from 'vue';
import { useLoginModal } from '@/account/login-modal';
import { useStore } from '@/store';

export default defineComponent({
  name: 'SidebarComponent',
  props: {
    visible: {
      type: Boolean,
      required: true,
    },
  },
  emits: ['update:visible'],
  setup(props, { emit }) {
    const { showLogin } = useLoginModal();
    const store = useStore();

    const closeSidebar = (): void => {
      emit('update:visible', false);
    };

    const handleLoginClick = (): void => {
      closeSidebar();
      setTimeout(() => {
        showLogin();
      }, 100);
    };

    const handleLogout = (): void => {
      closeSidebar();
      // Aquí puedes agregar la lógica de logout si es necesario
      console.log('Logout clicked');
    };

    // Ejemplo de propiedades computadas para mostrar/ocultar grupos
    const isAuthenticated = computed(() => {
      // Esto es un ejemplo - ajusta según tu implementación de autenticación
      return store.authenticated || false;
    });

    const showAdminSection = computed(() => {
      // Ejemplo - mostrar sección admin solo a usuarios autenticados
      return isAuthenticated.value;
    });

    return {
      closeSidebar,
      handleLoginClick,
      handleLogout,
      isAuthenticated,
      showAdminSection,
    };
  },
});
