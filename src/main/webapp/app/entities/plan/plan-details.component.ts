import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import PlanService from './plan.service';
import { type IPlan } from '@/shared/model/plan.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PlanDetails',
  setup() {
    const planService = inject('planService', () => new PlanService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const plan: Ref<IPlan> = ref({});

    const retrievePlan = async planId => {
      try {
        const res = await planService().find(planId);
        plan.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.planId) {
      retrievePlan(route.params.planId);
    }

    return {
      alertService,
      plan,

      previousState,
    };
  },
});
