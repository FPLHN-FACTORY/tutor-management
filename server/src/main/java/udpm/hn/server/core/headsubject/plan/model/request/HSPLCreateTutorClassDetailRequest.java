package udpm.hn.server.core.headsubject.plan.model.request;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.StudentTutor;
import udpm.hn.server.entity.TutorClass;

@Getter
@Setter
public class HSPLCreateTutorClassDetailRequest {

    private Integer numberOfClasses;
    private String tutorClassId;
    private Staff teacherConduct;
    private StudentTutor studentConduct;
    private TutorClass tutorClass;
    private Integer numberOfLectures;
    private Long startDate;
    private Long endDate;

}
