import { queryKey } from "@/constants/queryKey";
import {
  createDepartment,
  CreateUpdateDepartmentParams,
  getDepartments,
  getDetailDepartment,
  ParamsGetDepartment,
  updateDepartment,
} from "@/services/api/department.api";
import {
  useMutation,
  useQuery,
  useQueryClient,
  UseQueryReturnType,
} from "@tanstack/vue-query";
import { Ref } from "vue";

export const useGetDepartment = (
  params: Ref<ParamsGetDepartment>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getDepartments>>, Error> => {
  return useQuery({
    queryKey: [queryKey.admin.department.departmentList, params],
    queryFn: () => getDepartments(params),
    ...options,
  });
};

export const useCreateDepartment = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (params: CreateUpdateDepartmentParams) =>
      createDepartment(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.department.departmentList],
      });
    },
    onError: (error) => {
      console.log("🚀 ~ useCreateDepartment ~ error:", error);
    },
  });
};

export const useUpdateDepartment = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      departmentId,
      params,
    }: {
      departmentId: string;
      params: CreateUpdateDepartmentParams;
    }) => updateDepartment(departmentId, params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.department.departmentList],
      });
    },
    onError: (error) => {
      console.log("🚀 ~ useUpdateDepartment ~ error:", error);
    },
  });
};

export const useDetailDepartment = (
  departmentId: Ref<string | null>,
  options?: any
): UseQueryReturnType<
  Awaited<ReturnType<typeof getDetailDepartment>>,
  Error
> => {
  return useQuery({
    queryKey: [queryKey.admin.department.departmentDetail, departmentId],
    queryFn: () => getDetailDepartment(departmentId.value),
    ...options,
  });
};
