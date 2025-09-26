import { defineComponent } from 'vue';
import { useLoginModal } from '@/account/login-modal';

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

    const closeSidebar = (): void => {
      emit('update:visible', false);
    };

    const handleLoginClick = (): void => {
      closeSidebar();
      // Esperar un poco para que se cierre el sidebar antes de abrir el modal
      setTimeout(() => {
        showLogin();
      }, 100);
    };

    return {
      closeSidebar,
      handleLoginClick,
    };
  },
});
