<template>
    <div class="shadow-xl p-3 m-3 rounded-md flex h-full flex-col overflow-auto">
      <div class="flex justify-between items-center min-h-36">
        <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
          <v-icon name="bi-list-ul" scale="2" />
          <span class="ml-2 text-2xl">Danh sách lịch sử</span>
        </h2>
      </div>
      <div class="flex h-0 flex-1 flex-col">

        <tutor-table
            wrapperClassName="min-h-[410px]"
            :columns="columnsDepartment"
            :data-source="dataSource"
            :loading="loading"
            :pagination-params="paginationParams || {}"
            :total-pages="totalPages || 0"
            @update:pagination-params="$emit('update:paginationParams', $event)"
        >
          <template #bodyCell="{ column, record }">
            <div v-if="column.key === 'action'" class="space-x-2 text-center">
              <a-tooltip title="Chi tiết lịch sử" color="#FFC26E">
                <a-button
                  type="primary"
                  size="regular"
                  @click="$emit('handleOpenModalDetail', record)"
                  :icon="h(EyeOutlined)"
                />
              </a-tooltip>
            </div>
            <div v-else-if="column.key === 'typeFunction'" class="text-center">
                <a-tag v-if="record.typeFunction === 0" color="success">Tìm kiếm</a-tag>
                <a-tag v-else-if="record.typeFunction === 1" color="blue">Tạo mới</a-tag>
                <a-tag v-else-if="record.typeFunction === 3" color="orange">Cập nhật</a-tag>
                <a-tag v-else-if="record.typeFunction === 2" color="red">Xóa</a-tag>
                <a-tag v-else-if="record.typeFunction === 4" color="purple">Đăng nhập</a-tag>
                <a-tag v-else-if="record.typeFunction === 5" color="volcano">Đăng xuất</a-tag>
                <a-tag v-else-if="record.typeFunction === 6" color="geekblue">Đồng bộ</a-tag>
                <a-tag v-else-if="record.typeFunction === 7" color="magenta">Phê duyệt</a-tag>
                <a-tag v-else-if="record.typeFunction === 8" color="red">Từ chối</a-tag>
                <a-tag v-else-if="record.typeFunction === 9" color="blue">Thêm số lượng lớp tutor</a-tag>
                <a-tag v-else-if="record.typeFunction === 10" color="purple">Tạo kế hoạch</a-tag>
                <a-tag v-else-if="record.typeFunction === 11" color="volcano">Cập nhật kế hoạch</a-tag>
                <a-tag v-else-if="record.typeFunction === 12" color="magenta">Thêm giảng viên</a-tag>
                <a-tag v-else-if="record.typeFunction === 13" color="orange">Thêm lớp tutor</a-tag>
                <a-tag v-else-if="record.typeFunction === 14" color="red">Xóa lớp tutor</a-tag>
                <a-tag v-else-if="record.typeFunction === 15" color="orange">Thêm sinh viên tutor và phòng</a-tag>
                <a-tag v-else-if="record.typeFunction === 16" color="red">Tạo sinh viên tutor</a-tag>
            </div>
            <div v-else-if="column.key === 'status'" class="text-center">
                <a-tag v-if="record.status === 0" color="success">Thành công</a-tag>
                <a-tag v-else-if="record.status === 1" color="error">Thất bại</a-tag>
            </div>
            <div v-else-if="column.key === 'roleStaff'" class="text-center">
                <a-tag v-if="record.roleStaff === 'TRUONG_MON'" color="success">Trưởng môn</a-tag>
                <a-tag v-else-if="record.roleStaff === 'NGUOI_LAP_KE_HOACH'" color="error">Người lập kế hoạch</a-tag>
                <a-tag v-else-if="record.roleStaff === 'CHU_NHIEM_BO_MON'" color="error">Chủ nhiệm bộ môn</a-tag>
            </div>
            <div v-else-if="column.key === 'timeStampDate'" class="text-center">
                {{ getDateFormat(record.timeStampDate, true) }}
            </div>
          </template>
        </tutor-table>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import TutorTable from "@/components/ui/TutorTable/TutorTable.vue";
import { GetPlanLogResponse } from "@/services/api/planloghistory/planloghistory.api";
import { getDateFormat } from "@/utils/common.helper";
  import { EyeOutlined } from "@ant-design/icons-vue";
  import { ColumnType } from "ant-design-vue/es/table";
  import { h } from "vue";
  
  defineProps({
    dataSource: Array as () => GetPlanLogResponse[],
    loading: Boolean,
    paginationParams: Object as () => any,
    totalPages: Number,
  });
  
  const emit = defineEmits([
    "update:paginationParams",
    "handleOpenModalDetail"
  ]);
  

  const columnsDepartment: ColumnType[] = [
    {
      title: "STT",
      dataIndex: "orderNumber",
      key: "index",
      ellipsis: true,
      width: "50px",
    },
    {
      title: "Người dùng",
      dataIndex: "userName",
      key: "userName",
      ellipsis: true,
    },
    // {
    //   title: "Email",
    //   dataIndex: "email",
    //   key: "email",
    //   ellipsis: true,
    // },
    {
      title: "Vai trò thực hiện",
      dataIndex: "roleStaff",
      key: "roleStaff",
      ellipsis: true,
    },
    {
      title: "Tên kỳ",
      dataIndex: "namePlan",
      key: "namePlan",
      ellipsis: true,
      width: "120px",
    },
    {
      title: "Tên block",
      dataIndex: "nameBlock",
      key: "nameBlock",
      ellipsis: true,
    },
    {
      title: "Chức năng",
      dataIndex: "typeFunction",
      key: "typeFunction",
      ellipsis: true,
      width: "120px",
    },
    // {
    //   title: "Tên chức năng",
    //   dataIndex: "actionName",
    //   key: "actionName",
    //   ellipsis: true,
    //   width: "120px",
    // },
    {
      title: "Ngày thực hiện",
      dataIndex: "timeStampDate",
      key: "timeStampDate",
      ellipsis: true,
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
      width: "20px",
    },
    {
        title: "Hành động",
        key: "action",
        align: "center",
        width: "150px",
    },
  ];
  </script>
  