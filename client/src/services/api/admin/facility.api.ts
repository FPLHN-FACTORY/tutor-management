import { DefaultResponse, PaginationParams, PaginationResponse, ResponseList } from "@/types/api.common.ts";
import { Ref } from "vue";
import request from "../../request.ts";
import { PREFIX_API_FACILITY_ADMIN } from "@/constants/url.ts";
import { AxiosResponse } from "axios";

export interface ParamsGetFacility extends PaginationParams {
    name?: string,
    status?: string
}

export type FacilityResponse = ResponseList & {
    facilityName: string,
    facilityCode: string,
    facilityStatus: number,
    createdDate: number
}

export type FacilityDetailResponse = {
    id: string,
    facilityName: string,
    facilityCode: string,
    facilityStatus: number,
    createdDate: number
}

export const getFacilities = async (parmas: Ref<ParamsGetFacility>) => {
    const res = (await request({
        url: `${PREFIX_API_FACILITY_ADMIN}`,
        method: 'GET',
        params: parmas.value
    })) as AxiosResponse<
        DefaultResponse<PaginationResponse<Array<FacilityResponse>>>
    >

    return res.data;
}

export const getDetailFacility = async (facilityId: string | null) => {
    const res = (await request({
        url: `${PREFIX_API_FACILITY_ADMIN}/${facilityId}`,
        method: "GET",
    })) as AxiosResponse<DefaultResponse<FacilityDetailResponse>>;

    return res.data;
};

export const getFacilitySynchronize = async () => {
    const res: AxiosResponse<DefaultResponse<string>> = await request({
        url: `${PREFIX_API_FACILITY_ADMIN}/synchronize`,
        method: 'GET',
    });

    return res.data;
};