import { queryKey } from "@/constants/queryKey.ts";
import {
  createSubject,
  CreateUpdateSubjectParams,
  getDetailSubject,
  getSubjects,
  ParamsGetSubjects,
  updateSubject,
} from "../../api/admin/subject.api.ts";
import {
  useMutation,
  useQuery,
  useQueryClient,
  UseQueryReturnType,
} from "@tanstack/vue-query";
import { Ref } from "vue";

export const useGetSubject = (
  params: Ref<ParamsGetSubjects>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getSubjects>>, Error> => {
  return useQuery({
    queryKey: [queryKey.admin.subject.subjectList, params],
    queryFn: () => getSubjects(params),
    ...options,
  });
};

export const useCreateSubject = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (params: CreateUpdateSubjectParams) => createSubject(params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.subject.subjectList],
      });
    },
    onError: (error) => {
      console.log("🚀 ~ useCreateSubject ~ error:", error);
    },
  });
};

export const useUpdateSubject = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      subjectId,
      params,
    }: {
      subjectId: string;
      params: CreateUpdateSubjectParams;
    }) => updateSubject(subjectId, params),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [queryKey.admin.subject.subjectList],
      });
    },
    onError: (error) => {
      console.log("🚀 ~ useUpdateSubject ~ error:", error);
    },
  });
};

export const useDetailSubject = (
  subjectId: Ref<string | null>,
  options?: any
): UseQueryReturnType<Awaited<ReturnType<typeof getDetailSubject>>, Error> => {
  return useQuery({
    queryKey: [queryKey.admin.subject.subjectDetail, subjectId],
    queryFn: () => getDetailSubject(subjectId.value),
    ...options,
  });
};
