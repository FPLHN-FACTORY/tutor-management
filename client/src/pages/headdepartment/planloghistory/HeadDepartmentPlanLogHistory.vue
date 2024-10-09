<template>
    <div class="mt-4">
      <div class="flex justify-between items-center">
        <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
          <v-icon name="hi-office-building" scale="2" />
          <span class="m-2 text-3xl">Quản lý lịch sử</span>
        </h2>
      </div>
      <head-department-plan-log-history-filter
        :semesterOptions="semesterOptions"
        :semesterId="userInfo?.semesterId || null"
        :blockId="userInfo?.blockId || null"
      @filter="handleFilter"  />
      <head-department-plan-log-history-table
        :data-source="planLogHistoryData"
        :loading="isLoading || isFetching"
        :pagination-params="params"
        :total-pages="totalPages"
        @update:pagination-params="handlePaginationChange"
        @handleOpenModalDetail="handleOpenModalDetail"
      />
      <head-department-plan-log-history-detail
      :open="open"
      :plan-log-detail="planLogDetail || null"
      :loading="isLoadingDetail"
      @handleCloseModal="handleCloseModal"
      />
    </div>
  </template>
  
  <script lang="ts" setup>
import HeadDepartmentPlanLogHistoryTable from './HeadDepartmentPlanLogHistoryTable.vue';
import HeadDepartmentPlanLogHistoryDetail from './HeadDepartmentPlanLogHistoryDetail.vue';
import HeadDepartmentPlanLogHistoryFilter from './HeadDepartmentPlanLogHistoryFilter.vue';
import { computed, ref } from 'vue';
import { keepPreviousData } from '@tanstack/vue-query';
import { useDetailPlanLogHistory, useGetPlanLogHistory } from '@/services/service/planloghistory/planloghistory.action';
import { GetPlanLogResponse, ParamsGetPlanLog } from '@/services/api/planloghistory/planloghistory.api';
import { useAuthStore } from '@/stores/auth';
import { useGetSemesterOptions } from '@/services/service/common.action';

const auth = useAuthStore();
const userInfo = computed(() => auth.user);

const params = ref<ParamsGetPlanLog>({
  page: 1,
  size: 10,
  staffId: userInfo.value?.userId,
  blockId: null,
  planId: null,
  semesterId: userInfo.value?.semesterId,
  roleStaff: 'NGUOI_LAP_KE_HOACH,TRUONG_MON,CHU_NHIEM_BO_MON', 
  facilityId: userInfo.value?.facilityId,
  logCodeRole: 'CHU_NHIEM_BO_MON', 
  fromDate: null,
  toDate: null,
});

const open = ref(false);
const planLogHistoryId = ref<string | null>(null);

const { data, isLoading, isFetching } = useGetPlanLogHistory(params, {
  refetchOnWindowFocus: false,
  placeholderData: keepPreviousData,
});


const handlePaginationChange = (newParams: ParamsGetPlanLog) => {
  params.value = { ...params.value, ...newParams };
};

const handleFilter = (newParams: ParamsGetPlanLog) => {
  params.value = { ...params.value, ...newParams };
};

const { data: semesterOptionsData } = useGetSemesterOptions();
const semesterOptions = computed(() =>
    semesterOptionsData?.value?.data.map((semester) => ({
      value: semester.id,
      label: semester.name,
    }))
);

const handleOpenModalDetail = (record: GetPlanLogResponse) => {
    planLogHistoryId.value = record.id;
  open.value = true;
};

const { data: dataDetail, isLoading: isLoadingDetail } = useDetailPlanLogHistory(
    planLogHistoryId,
  {
    refetchOnWindowFocus: false,
    enabled: () => !!planLogHistoryId.value,
  }
);

const handleCloseModal = () => {
  open.value = false;
  planLogHistoryId.value = null;
};

const planLogHistoryData = computed(() => data?.value?.data?.data || []);
const totalPages = computed(() => data?.value?.data?.totalPages || 0);
const planLogDetail = computed(() => dataDetail.value?.data || null);
  </script>
  