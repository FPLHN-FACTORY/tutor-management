<template>
    <div>
        <a-modal 
            :open="open" 
            title="Chi Tiết" 
            @cancel="handleClose" 
            destroyOnClose 
            centered
            :footer="null" 
            width="800px">
            
            <div v-if="isLoadingDetail" class="flex justify-center items-center">
                <a-spin />
            </div>
            
            <div v-else>
                <div class="shadow-md p-1 rounded-md my-15">
                    <a-descriptions class="p-1">
                        <a-descriptions-item :span="2" label="Người dùng">{{ planLogDetail?.userName }}</a-descriptions-item>
                        <a-descriptions-item :span="2" label="Email">{{ planLogDetail?.email }}</a-descriptions-item>
                        <template v-if="planLogDetail?.typeFunction !== 16 && planLogDetail?.typeFunction !== 15">
                                <a-descriptions-item :span="2" label="Tên kỳ">{{ planLogDetail?.namePlan }}</a-descriptions-item>
                                <a-descriptions-item :span="2" label="Tên block">{{ planLogDetail?.nameBlock }}</a-descriptions-item>
                        </template>

                        <a-descriptions-item v-if="planLogDetail?.typeFunction === 16 || planLogDetail?.typeFunction === 15" :span="2" label="Sinh viên đã thêm">{{ planLogDetail?.studentTutor }}</a-descriptions-item>
                        <a-descriptions-item v-if="planLogDetail?.typeFunction === 15" :span="2" label="Phòng đã thêm">{{ planLogDetail?.roomPlan }}</a-descriptions-item>

                        <!-- nếu chức năng là tạo kế hoạch thì mới hiển thị 3 cái này -->
                        <a-descriptions-item v-if="planLogDetail?.typeFunction === 10 || planLogDetail?.typeFunction === 11" :span="2" label="Thời gian bắt đầu thêm môn">{{ getDateFormat(planLogDetail?.startDate, true)}}</a-descriptions-item>
                        <a-descriptions-item v-if="planLogDetail?.typeFunction === 10 || planLogDetail?.typeFunction === 11" :span="2" label="Mô tả">{{ planLogDetail?.description}}</a-descriptions-item>
                        <a-descriptions-item v-if="planLogDetail?.typeFunction === 10 || planLogDetail?.typeFunction === 11" :span="2" label="Thời gian kết thúc thêm">{{ getDateFormat(planLogDetail?.endDate, true) }}</a-descriptions-item>
                        
                        <a-descriptions-item v-if="planLogDetail?.typeFunction === 9" :span="2" label="Số lớp">{{ planLogDetail?.numberOfClass}}</a-descriptions-item>
                        <a-descriptions-item v-if="planLogDetail?.typeFunction === 9" :span="2" label="Mô buổi">{{ planLogDetail?.numberOfLecture}}</a-descriptions-item>
                        
                        <a-descriptions-item v-if="planLogDetail?.typeFunction === 12 || planLogDetail?.typeFunction === 13 || planLogDetail?.typeFunction === 14" :span="2" label="Mã lớp">{{ planLogDetail?.codeTutorClassDetail}}</a-descriptions-item>
                        <a-descriptions-item v-if="planLogDetail?.typeFunction === 12" :span="2" label="Giảng viên">{{ planLogDetail?.staffInfo}}</a-descriptions-item>

                        <a-descriptions-item v-if="planLogDetail?.typeFunction === 9" :span="2" label="Hình thức">
                        {{ planLogDetail?.formality === 0 ? 'Online' : 'Offline' }}
                        </a-descriptions-item>

                        
                        <a-descriptions-item :span="2" label="Ngày thực hiện">{{ getDateFormat(planLogDetail?.timeStampDate, true) }}</a-descriptions-item>
                        <a-descriptions-item :span="2" label="Tên chức năng">{{ planLogDetail?.actionName }}</a-descriptions-item>
                        <a-descriptions-item label="Chức năng">
                            <a-tag :color="getTagColorLog(planLogDetail?.status)">
                                {{ getTagStatusLog(planLogDetail?.typeFunction) }}
                            </a-tag>
                        </a-descriptions-item>

                        <a-descriptions-item v-if="planLogDetail?.typeFunction === 8" :span="2" label="Lý do từ chối">{{ planLogDetail?.rejectNote}}</a-descriptions-item>

                        <a-descriptions-item :span="2" label="Vai trò người thực hiện">
                            <div v-if="planLogDetail?.roleStaff === 'TRUONG_MON'">
                                <a-tag color="success">Trưởng môn</a-tag>
                            </div>
                            <div v-else-if="planLogDetail?.roleStaff === 'NGUOI_LAP_KE_HOACH'">
                                <a-tag color="error">Người lập kế hoạch</a-tag>
                            </div>
                            <div v-else-if="planLogDetail?.roleStaff === 'CHU_NHIEM_BO_MON'">
                                <a-tag color="error">Chủ nhiệm bộ môn</a-tag>
                            </div>
                        </a-descriptions-item>
                        <a-descriptions-item :span="2" label="Trạng thái">
                            <div v-if="planLogDetail?.status === 0">
                                <a-tag color="success">Thành công</a-tag>
                            </div>
                            <div v-else-if="planLogDetail?.status === 1">
                                <a-tag color="error">Thất bại</a-tag>
                            </div>
                        </a-descriptions-item>
                    </a-descriptions>
                </div>
            </div>
        </a-modal>
    </div>
</template>

<script lang="ts" setup>
import { getDateFormat, getTagColorLog, getTagStatusLog } from "@/utils/common.helper";
import { defineProps, defineEmits, watch } from "vue";

const props = defineProps({
    open: Boolean,
    planLogDetail: Object as () => any | null,
    isLoadingDetail: Boolean,
});

const emit = defineEmits(["handleCloseModal"]);

const handleClose = () => {
    emit("handleCloseModal");
};

watch(
    () => props.planLogDetail,
    (newVal) => {
        if (newVal) {
            console.log(newVal);
        }
    },
    { immediate: true }
);
</script>
