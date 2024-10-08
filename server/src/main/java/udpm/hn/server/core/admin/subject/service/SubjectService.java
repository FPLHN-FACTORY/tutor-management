package udpm.hn.server.core.admin.subject.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.admin.subject.model.request.CreateUpdateSubjectRequest;
import udpm.hn.server.core.admin.subject.model.request.SubjectRequest;
import udpm.hn.server.core.admin.subject.model.response.SubjectResponse;
import udpm.hn.server.core.common.base.ResponseObject;

import java.util.List;

public interface SubjectService {

    ResponseObject<?> getAllSubject(SubjectRequest request);

    ResponseObject<?> createSubject(@Valid CreateUpdateSubjectRequest request);

    ResponseObject<?> updateSubject(String subjectId, @Valid CreateUpdateSubjectRequest request);

    ResponseObject<?> changeSubjectStatus(String subjectId);

    ResponseObject<?> getSubjectById(String subjectId);

    ResponseObject<?> getAllSubjectByStaffId(String staffId);

}
