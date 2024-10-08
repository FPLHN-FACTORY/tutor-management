<template>
  <div class="shadow-xl p-3 rounded-md flex h-full flex-col overflow-auto">
    <div class="flex justify-between items-center min-h-[56px]">
      <h2 class="flex items-center text-primary text-3xl font-semibold p-2">
        <span class="text-xl">Danh sách bộ môn theo cơ sở</span>
      </h2>
      <a-popconfirm placement="bottom" ok-text="Yes" cancel-text="No" @confirm="handleSync">
        <template #title>
          <p>Bạn chắc chắn muốn đồng bộ chứ?</p>
        </template>
        <a-button type="primary" size="large" class="m-4 flex justify-between items-center"
          :disabled="isSyncing || loading">
          <v-icon name="bi-arrow-repeat" scale="1.5" class="me-1" />
          Đồng bộ
        </a-button>
      </a-popconfirm>
    </div>
    <div class="flex h-0 flex-1 flex-col">
      <tutor-table wrapperClassName="min-h-[410px]" :columns="columnsDepartmentFacility" :data-source="dataSource"
        :loading="loading" :pagination-params="paginationParams || {}" :total-pages="totalPages || 0"
        @update:pagination-params="$emit('update:paginationParams', $event)">
        <template #bodyCell="{ column, record }">
          <div v-if="column.key === 'action'" class="space-x-2 text-center">
            <a-tooltip title="Danh sách chuyên ngành thuộc cơ sở của bộ môn" color="#FFC26E">
              <a-button type="primary" size="regular" @click="$emit('handleGetMajorFacility', record)"
                :icon="h(EyeOutlined)" />
            </a-tooltip>
          </div>
          <div v-else-if="column.key === 'departmentFacilityStatus'" class="text-center">
            <a-tag v-if="record.departmentFacilityStatus === 0" color="success">
              Hoạt động
            </a-tag>
            <a-tag v-else-if="record.departmentFacilityStatus === 1" color="error">
              Không hoạt động
            </a-tag>
          </div>
        </template>
      </tutor-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { ERROR_MESSAGE } from "@/constants/message.constant";
import { FacilityResponse } from "@/services/api/admin/department.api";
import { useDepartmentCampusSynchronize } from "@/services/service/admin/department.action";
import { useMajorCampusSynchronize } from "@/services/service/admin/major.action";
import { EyeOutlined } from "@ant-design/icons-vue";
import { ColumnType } from "ant-design-vue/es/table";
import { h } from "vue";
import { toast } from "vue3-toastify";

defineProps({
  dataSource: Array as () => FacilityResponse[],
  loading: Boolean,
  paginationParams: Object as () => any,
  totalPages: Number,
});

const emit = defineEmits([
  "update:paginationParams",
  "handleGetMajorFacility",
  "dataSynced"
]);

const { mutate: onSyncDepartmentCampus, isLoading: isSyncing } = useDepartmentCampusSynchronize();

const { mutate: onSyncMajorCampusSynchronize, } = useMajorCampusSynchronize();

const handleSync = async () => {
  try {
    // Gọi hàm đồng bộ bộ môn và chờ cho nó hoàn thành
    await new Promise((resolve, reject) => {
      onSyncDepartmentCampus(undefined, {
        onSuccess: () => {
          resolve(); // Nếu thành công, gọi resolve
        },
        onError: (error: any) => {
          toast.error(
              error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
          );
          reject(new Error("Department sync failed")); // Nếu có lỗi, gọi reject
        },
      });
    });

    // Nếu hàm trên thành công, gọi hàm đồng bộ chuyên ngành
    await new Promise((resolve, reject) => {
      onSyncMajorCampusSynchronize(undefined, {
        onSuccess: () => {
          toast.success("Đồng bộ chuyên ngành theo cơ sở thành công!");
          resolve(); // Nếu thành công, gọi resolve
        },
        onError: (error: any) => {
          toast.error(
              error?.response?.data?.message || ERROR_MESSAGE.SOMETHING_WENT_WRONG
          );
          reject(new Error("Major sync failed")); // Nếu có lỗi, gọi reject
        },
      });
    });

    // Phát sự kiện đồng bộ thành công
    emit("dataSynced");
  } catch (error) {
    console.log("Có lỗi xảy ra trong quá trình đồng bộ: " + error.message);
  }
};

const columnsDepartmentFacility: ColumnType[] = [
  {
    title: "STT",
    dataIndex: "orderNumber",
    key: "index",
    ellipsis: true,
  },
  {
    title: "Tên cơ sở",
    dataIndex: "facilityName",
    key: "facilityName",
    ellipsis: true,
  },
  {
    title: "Chủ nhiệm bộ môn",
    dataIndex: "profileStaff",
    key: "profileStaff",
    ellipsis: true,
  },
  {
    title: "Hành động",
    key: "action",
    align: "center",
    width: "150px",
  },
];
</script>
