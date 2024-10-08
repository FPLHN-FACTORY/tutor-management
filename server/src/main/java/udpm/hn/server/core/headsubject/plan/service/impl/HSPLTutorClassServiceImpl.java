package udpm.hn.server.core.headsubject.plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headsubject.plan.model.request.*;
import udpm.hn.server.core.headsubject.plan.model.response.HSPLTutorClassResponse;
import udpm.hn.server.core.headsubject.plan.repository.*;
import udpm.hn.server.core.headsubject.plan.service.HSPLTutorClassService;
import udpm.hn.server.entity.*;
import udpm.hn.server.infrastructure.constant.Format;
import udpm.hn.server.infrastructure.constant.Role;
import udpm.hn.server.utils.Helper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class HSPLTutorClassServiceImpl implements HSPLTutorClassService {

    private final HSPLStaffsRepository staffRepository;

    private final HSPLSubjectRepository subjectRepository;

    private final HSPLPlansRepository planRepository;

    private final HSPLTutorClassRepository tutorClassRepository;

    private final HSPLTutorClassDetailRepository tutorClassDetailRepository;

    private final HSPLNotificationRepository notificationRepository;

    @Override
    public ResponseObject<?> createTutorClass(HSPLCreateTutorClassRequest request) {
        try {
            Plan plan = planRepository.findById(request.getPlanId()).orElse(null);
            Subject subject = subjectRepository.findById(request.getSubjectId()).orElse(null);
            TutorClass tutorClass = new TutorClass();
            tutorClass.setPlan(plan); // Giả định bạn có thuộc tính này
            tutorClass.setSubject(subject); // Giả định bạn có thuộc tính này
            tutorClass.setNumberOfClasses(request.getNumberOfClasses());
            tutorClass.setNumberOfLectures(request.getNumberOfLectures());
            tutorClass.setFormat(Format.fromString(request.getFormat()));
            TutorClass savedTutorClass = tutorClassRepository.save(tutorClass);

            //Thêm 2 thông báo đến NGUOI_LAP_KE_HOACH và CHU_NHIEM_BO_MON
            List<Notification> listNotificationSave = new ArrayList<>();
            for (String role : List.of(Role.NGUOI_LAP_KE_HOACH.toString(), Role.CHU_NHIEM_BO_MON.toString())) {
                Notification item = new Notification();
                item.setPlan(plan);
                item.setContent("Kế hoạch " + plan.getDescription() + " đã được trưởng môn thêm lớp tutor.");
                item.setStaff(plan.getPlanner());
                item.setSentTo(role);
                listNotificationSave.add(item);
            }
            notificationRepository.saveAll(listNotificationSave);

            String code = subject.getCode();
            // Lưu tutor detail dựa vào số lương
            if (tutorClass.getNumberOfClasses() != null && tutorClass.getNumberOfClasses() > 0) {
                for (int i = 0; i < tutorClass.getNumberOfClasses(); i++) {
                    TutorClassDetail tutorClassDetail = new TutorClassDetail();
                    tutorClassDetail.setTutorClass(savedTutorClass);
                    tutorClassDetail.setNumberOfLectures(request.getNumberOfLectures());
                    tutorClassDetail.setCode(Helper.generateTutorClassCodeFromName(code));
                    tutorClassDetailRepository.save(tutorClassDetail);
                }
            }
            return new ResponseObject<>(tutorClass, HttpStatus.CREATED, "Tạo mới lớp tutor thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cập nhật số lượng lớp tutor không thành công: " + e.getMessage());
        }
    }

    @Override
    public ResponseObject<?> updateTutorClass(String id, HSPLUpdateTutorClassRequest request) {
        try {
            TutorClass tutorClass = tutorClassRepository.findById(id).orElse(null);
            if (tutorClass == null) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Id không hợp lệ");
            }
            tutorClass.setFormat(Format.fromString(request.getFormat()));
            tutorClass.setNumberOfLectures(request.getNumberOfLectures());
            tutorClassRepository.save(tutorClass);

            return new ResponseObject<>(tutorClass, HttpStatus.OK, "Cập nhật trạng thái lớp tutor thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cập nhật trạng thái lớp tutor không thành công: " + e.getMessage());
        }
    }

    @Override
    public ResponseObject<?> getDetailTutorClass(String id) {
        try {
            HSPLTutorClassResponse tutorClass = tutorClassRepository.getDetailTutorClass(id);
            if (tutorClass == null) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Không tồn tại");
            }
            return new ResponseObject<>(tutorClass, HttpStatus.OK, "Lấy lớp tutor thành công1");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lỗi khi lấy chi tiết: " + e.getMessage());
        }
    }

    @Override
    public ResponseObject<?> getTutorClasses(HSPLSubjectListRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(tutorClassRepository.getTutorClasses(pageable, request)
                ), HttpStatus.OK, "Lấy danh sách lớp môn thành công!");
    }

    @Override
    public ResponseObject<?> getTutorClassDetail(HSPLTutorClassDetailRequest request) {
        if (request.getQuery() != null) {
            request.setQuery(request.getQuery().trim());
        }
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(tutorClassDetailRepository.getTutorClassDetailByTutorClassId(pageable, request)),
                HttpStatus.OK,
                "Lấy danh sách lớp môn thành công!"
        );
    }

    @Override
    public ResponseObject<?> updateTutorClassDetail(String id, HSPLUpdateTutorClassDetailRequest request) {
        try {
            TutorClassDetail tutorClassDetail = tutorClassDetailRepository.findById(id).orElse(null);
            if (tutorClassDetail == null) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lớp tutor không tồn tại!");
            }

            Staff staff = staffRepository.findById(request.getStaffId()).orElse(null);
            if (staff == null) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Giáo viên không tồn tại!");
            }
            tutorClassDetail.setTeacherConduct(staff);
            tutorClassDetailRepository.save(tutorClassDetail);

            //Thông báo đến GIANG_VIEN đc thêm vào
            Notification item = new Notification();
            item.setPlan(tutorClassDetail.getTutorClass().getPlan());
            item.setContent("Bạn đã được trưởng môn thêm vào lớp tutor " + tutorClassDetail.getTutorClass().getSubject().getName() + " .");
            item.setStaff(staff);
            item.setSentTo(Role.GIANG_VIEN + ": " + staff.getId());
            notificationRepository.save(item);

            return new ResponseObject<>(tutorClassDetail, HttpStatus.OK, "Cập nhật giáo viên lớp tutor thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cập nhật giáo viên lớp tutor không thành công: " + e.getMessage());
        }
    }

    @Override
    public ResponseObject<?> deleteTutorClassDetail(String id) {
        try {
            TutorClassDetail tutorClassDetail = tutorClassDetailRepository.findById(id).orElse(null);
            if (tutorClassDetail == null) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lớp tutor không tồn tại!");
            }

            tutorClassDetailRepository.delete(tutorClassDetail);

            TutorClass tutorClass = tutorClassDetail.getTutorClass();
            List<TutorClassDetail> list = tutorClassDetailRepository.findByTutorClass(tutorClass);
            if (list.isEmpty()) {
                tutorClassRepository.delete(tutorClass);
            }
            return new ResponseObject<>(tutorClassDetail, HttpStatus.OK, "Xóa lớp tutor thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Xóa lớp tutor thất bại: " + e.getMessage());
        }
    }

    @Override
    public ResponseObject<?> addTutorClassDetail(String id) {
        try {
            TutorClassDetail tutorClassDetail = tutorClassDetailRepository.findById(id).orElse(null);
            if (tutorClassDetail == null) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lớp tutor không tồn tại!");
            }

            TutorClassDetail newTutorClassDetail = new TutorClassDetail();
            newTutorClassDetail.setTutorClass(tutorClassDetail.getTutorClass());
            newTutorClassDetail.setCode(Helper.generateTutorClassCodeFromName(tutorClassDetail.getTutorClass().getSubject().getCode()));
            tutorClassDetailRepository.save(newTutorClassDetail);

            return new ResponseObject<>(tutorClassDetail, HttpStatus.OK, "Thêm lớp tutor thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Thm lớp tutor thất bại: " + e.getMessage());
        }
    }

}
