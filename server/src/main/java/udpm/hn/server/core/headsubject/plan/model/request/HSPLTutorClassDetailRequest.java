package udpm.hn.server.core.headsubject.plan.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import udpm.hn.server.core.common.base.PageableRequest;

@Setter
@Getter
@ToString
public class HSPLTutorClassDetailRequest extends PageableRequest {

    private String planId;
    private String userId;
    private String teacherId;
    private String query;
    private String semesterId;
    private String facilityId;

}
