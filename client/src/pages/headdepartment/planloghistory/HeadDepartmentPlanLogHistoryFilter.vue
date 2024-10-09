<template>
    <div class="shadow-md p-3 rounded-md m-3">
        <h2 class="p-4 flex items-center text-primary text-3xl font-semibold">
            <v-icon name="co-filter" scale="2" />
            <span class="ml-2 text-2xl">Bộ lọc</span>
        </h2>
        <a-form layout="vertical" class="mb-4 grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3 p-5">
            <a-form-item label="Chức vụ thực hiện" class="col-span-1">
                <a-select
                    :value="params.roleStaff"
                    @change="onRoleStaffChange"
                    placeholder="Chọn chức vụ"
                    allowClear
                >
                    <a-select-option :value="'NGUOI_LAP_KE_HOACH,CHU_NHIEM_BO_MON,TRUONG_MON'">Chọn tất cả</a-select-option>
                    <a-select-option :value="'NGUOI_LAP_KE_HOACH'">Người lập kế hoạch</a-select-option>
                    <a-select-option :value="'CHU_NHIEM_BO_MON'">Chủ nhiệm bộ môn</a-select-option>
                    <a-select-option :value="'TRUONG_MON'">Trưởng môn</a-select-option>
                </a-select>
            </a-form-item>

            <a-form-item label="Học kỳ" class="col-span-1">
                <a-select
                    :value="params.semesterId"
                    @change="onSemesterChange"
                    placeholder="Chọn kỳ học"
                    allowClear
                >
                    <a-select-option :value="null">Chọn tất cả</a-select-option>
                    <a-select-option
                        v-for="option in props.semesterOptions"
                        :key="option.value"
                        :value="option.value"
                    >
                        {{ option.label }}
                    </a-select-option>
                </a-select>
            </a-form-item>

            <a-form-item label="Block" class="col-span-1">
                <a-select
                    :value="params.blockId"
                    @change="onSelectChange('blockId', $event)"
                    placeholder="Chọn block"
                    allowClear
                >
                    <a-select-option :value="null">Chọn tất cả</a-select-option>
                    <a-select-option
                        v-for="block in blockOptionsComputed"
                        :key="block.value"
                        :value="block.value"
                    >
                        {{ block.label }}
                    </a-select-option>
                </a-select>
            </a-form-item>

            <a-form-item label="Chức năng" class="col-span-1">
                <a-select
                    :value="params.logType"
                    @change="onSelectChange('logType', $event)"
                    placeholder="Loại chức năng"
                    allowClear
                >
                    <a-select-option :value="null">Chọn tất cả</a-select-option>
                    <a-select-option
                        v-for="option in logFunctionTypeOptions"
                        :key="option.value"
                        :value="option.value"
                    >
                        {{ option.label }}
                    </a-select-option>
                </a-select>
            </a-form-item>

            <a-form-item label="Ngày thực hiện" class="col-span-1">
                <a-range-picker
                    @change="(dates) => {
                        onChange('fromDate', dates[0]);
                        onChange('toDate', dates[1]);
                    }"
                    :placeholder="['Từ ngày', 'Đến ngày']"
                    :format="'DD/MM/YYYY'"
                    style="width: 100%"
                />
            </a-form-item>

            <a-form-item label="Trạng thái" class="col-span-1">
                <a-select
                    :value="params.status"
                    @change="onSelectChange('status', $event)"
                    placeholder="Chọn trạng thái"
                    allowClear
                >
                    <a-select-option :value="null">Chọn tất cả</a-select-option>
                    <a-select-option :value="'ACTIVE'">Thành công</a-select-option>
                    <a-select-option :value="'INACTIVE'">Thất bại</a-select-option>
                </a-select>
            </a-form-item>
        </a-form>
    </div>
</template>

<script setup lang="ts">
import dayjs from 'dayjs';
import { debounce } from 'lodash';
import { ref, watch, computed } from 'vue';
import { useGetBlockOptions } from '@/services/service/common.action';
import { useAuthStore } from '@/stores/auth';
import { ParamsGetPlanLog } from '@/services/api/planloghistory/planloghistory.api';
import { getBlockOptions } from '@/services/api/common.api';
import { formatBlockName } from '@/utils/common.helper';

const emit = defineEmits(["filter"]);
const debouncedEmit = debounce(() => {
    emit("filter", params.value);
}, 2000);

const auth = useAuthStore();
const userInfo = computed(() => auth.user);
const props = defineProps({
  semesterOptions: Array as () => any,
  semesterId: String,
  blockId: String,
});



const logFunctionTypeOptions = [
    { label: "Phê duyệt", value: 7 },
    { label: "Từ chối", value: 8 },
    { label: "Thêm số lượng lớp tutor", value: 9 },
    { label: "Tạo kế hoạch", value: 10 },
    { label: "Cập nhật kế hoạch", value: 11 },
    { label: "Thêm nhân viên", value: 12 },
    { label: "Thêm lớp tutor", value: 13 },
    { label: "Xóa lớp tutor", value: 14 },
    { label: "Thêm nhân viên tutor và phòng", value: 15 },
    { label: "Tạo nhân viên tutor", value: 16 },
    { label: "Xóa lớp tutor", value: 14 },
    { label: "Tìm kiếm", value: 0 },
    { label: "Tạo mới", value: 1 },
    { label: "Cập nhật", value: 3 },
    { label: "Xóa", value: 2 },
    { label: "Đồng bộ", value: 6 },
];



type ParamsFilterPlanLog = {
    staffId?: string | null;
    blockId: string | null;
    planId: string | null;
    roleStaff: string | null;
    facilityId?: string | null;
    logCodeRole: string | null;
    logType: number | null;
    fromDate: number | null;
    toDate: number | null;
    status: number | null;
    semesterId: string | null;
    
};

const params = ref<ParamsFilterPlanLog>({
    staffId: userInfo.value?.userId,
    blockId: userInfo.value?.blockId || null,
    planId: null,
    semesterId: userInfo.value?.semesterId || null,
    roleStaff: 'NGUOI_LAP_KE_HOACH,CHU_NHIEM_BO_MON,TRUONG_MON',
    facilityId: userInfo.value?.facilityId,
    logCodeRole: 'TRUONG_MON',
    logType: null,
    fromDate: null,
    toDate: null,
    status: null,
});

function onSemesterChange(value: string) {
    params.value.semesterId = value;

    if (value) {
        // Gọi API để lấy block tương ứng với học kỳ
        const { data: blockData } = useGetBlockOptions(value);
        blockOptionsComputed.value = blockData.value ? blockData.value.data.map((block) => ({
            value: block.id,
            label: block.name,
        })) : [];
        
        // Thiết lập blockId thành null nếu có giá trị mới
        params.value.blockId = null;
    } else {
        // Nếu chọn tất cả học kỳ, thiết lập blockId thành null
        params.value.blockId = null;
    }
}

function onRoleStaffChange(value: string) {
    params.value.roleStaff = value;
    emit("filter", params.value);
}


function onSelectChange(key: keyof ParamsFilterPlanLog, value: string) {
    params.value[key] = value;
    emit("filter", params.value);
}

function onChange(key: keyof ParamsGetPlanLog, event: any) {
    if (key === "fromDate") {
        const selectedDate = event ? dayjs(event).startOf('day').valueOf() : null;
        params.value[key] = selectedDate;
    } else if (key === "toDate") {
        const selectedDate = event ? dayjs(event).endOf('day').valueOf() : null;
        params.value[key] = selectedDate;
    } else {
        params.value[key] = event;
    }
}

const { data: blockData } = useGetBlockOptions(params.value.semesterId);

const blockOptionsComputed = computed(() => {
    return blockData.value ? blockData.value.data.map((block) => ({
        value: block.id,
        label: block.name,
    })) : [];
});


watch(
    params,
    () => {
        debouncedEmit();
    },
    { deep: true }
);
</script>
