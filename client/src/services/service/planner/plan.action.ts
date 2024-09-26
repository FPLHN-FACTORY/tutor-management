import {
  createPlan,
  CreateUpdatePlanParams,
  getDetailPlan,
  getPlans, getSemesterInfo,
  ParamsGetPlans, ParamsGetSemesterInfo, updatePlan
} from "@/services/api/planner/plan.api.ts";
import {Ref} from "vue";
import {useMutation, useQuery, useQueryClient, UseQueryReturnType} from "@tanstack/vue-query";
import {queryKey} from "@/constants/queryKey.ts";

export const useGetPlans = (
    params: Ref<ParamsGetPlans>,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getPlans>>, Error> => {
  return useQuery({
    queryKey: [queryKey.planner.plan.planList, params],
    queryFn: () => getPlans(params),
    ...options,
  });
};

export const useGetSemesterInfo = (
    params: ParamsGetPlans,
    options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getSemesterInfo>>, Error> => {
  console.log(params);
  return useQuery({
    queryKey: [queryKey.planner.plan.semesterInfo, params],
    queryFn: () => getSemesterInfo(params),
    ...options,
  });
};

export const useCreatePlan = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (params: CreateUpdatePlanParams) => createPlan(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.planner.plan.planList],
      });
    },
    onError: (error) => {
      console.log("🚀 ~ useCreatePlan ~ error:", error);
    },
  });
};

export const useUpdatePlan = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({
                   planId,
                   params,
                 }: {
      planId: string;
      params: CreateUpdatePlanParams;
    }) => updatePlan(planId, params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.planner.plan.planList],
      });
    },
    onError: (error) => {
      console.log("🚀 ~ useUpdatePlan ~ error:", error);
    },
  });
};

export const useDetailPlan = (
  planId: Ref<string | null>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getDetailPlan>>, Error> => {
  return useQuery({
    queryKey: [queryKey.planner.plan.planDetail, planId],
    queryFn: () => getDetailPlan(planId.value),
    ...options,
  });
};
