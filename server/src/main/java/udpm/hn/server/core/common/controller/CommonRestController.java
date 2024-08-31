package udpm.hn.server.core.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.common.model.request.StaffSearchRequest;
import udpm.hn.server.core.common.service.CommonServiceHelper;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_COMMON)
@RequiredArgsConstructor
public class CommonRestController {

    private final CommonServiceHelper commonServiceHelper;

    @GetMapping("/semester")
    public ResponseEntity<?> getSemester() {
        return Helper.createResponseEntity(commonServiceHelper.getSemesterInfo());
    }

    @GetMapping("/staff/search")
    public ResponseEntity<?> searchStaff(StaffSearchRequest request) {
        return Helper.createResponseEntity(commonServiceHelper.getStaffSearch(request));
    }

    @GetMapping("/department")
    public ResponseEntity<?> getDepartment() {
        return Helper.createResponseEntity(commonServiceHelper.getAllDepartmentSubject());
    }

}
