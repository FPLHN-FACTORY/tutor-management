package udpm.hn.server.utils;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import udpm.hn.server.infrastructure.constant.SessionConstant;

@Component
@RequiredArgsConstructor
class SessionHelper {

    private final HttpSession httpSession;

    public String getCurrentUserEmail() {
        return httpSession.getAttribute(SessionConstant.CURRENT_USER_EMAIL).toString();
    }

    public String getCurrentUserId() {
        return httpSession.getAttribute(SessionConstant.CURRENT_USER_ID).toString();
    }

    public String getCurrentUserFacilityId() {
        return httpSession.getAttribute(SessionConstant.CURRENT_USER_FACILITY_ID).toString();
    }

    public String getCurrentUserDepartmentId() {
        return httpSession.getAttribute(SessionConstant.CURRENT_USER_DEPARTMENT_ID).toString();
    }

    public String getCurrentUserDepartmentFacilityId() {
        return httpSession.getAttribute(SessionConstant.CURRENT_USER_DEPARTMENT_FACILITY_ID).toString();
    }

    public String getCurrentUserMajorFacilityId() {
        return httpSession.getAttribute(SessionConstant.CURRENT_USER_MAJOR_FACILITY_ID).toString();
    }

    public String getCurrentUserRole() {
        return httpSession.getAttribute(SessionConstant.CURRENT_USER_ROLE).toString();
    }

    public String getCurrentSemesterId() {
        return httpSession.getAttribute(SessionConstant.CURRENT_SEMESTER_ID).toString();
    }

    public String getCurrentBlockId() {
        return httpSession.getAttribute(SessionConstant.CURRENT_BLOCK_ID).toString();
    }

}
