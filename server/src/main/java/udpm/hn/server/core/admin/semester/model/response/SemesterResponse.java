package udpm.hn.server.core.admin.semester.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface SemesterResponse extends IsIdentify, HasOrderNumber {

    String getSemesterName();

    Long getSemesterYear();

    Long getStartTime();

    Long getEndTime();

    Long getStartTimeFirstBlock();

    Long getEndTimeFirstBlock();

    Long getStartTimeSecondBlock();

    Long getEndTimeSecondBlock();

}