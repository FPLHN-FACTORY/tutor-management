package udpm.hn.server.core.admin.staff.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.staff.model.response.HOStaffDepartmentResponse;
import udpm.hn.server.core.admin.staff.model.response.HOStaffMajorFacilityResponse;
import udpm.hn.server.core.admin.staff.model.response.HOStaffMajorResponse;
import udpm.hn.server.entity.MajorFacility;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.MajorFacilityRepository;

import java.util.List;

@Repository
public interface HOStaffMajorFacilityRepository extends MajorFacilityRepository {

    @Query(value = """
            SELECT 
                   ROW_NUMBER() OVER(
            	            ORDER BY smf.id DESC) AS orderNumber,
                   f.name AS facilityName,
            	   d.name AS departmentName,
            	   m.name AS majorName,
            	   smf.id AS id
            FROM staff_major_facility smf
            JOIN major_facility mf ON mf.id = smf.id_major_facility
            JOIN major m ON m.id = mf.id_major
            JOIN department_facility df ON df.id = mf.id_department_facility
            JOIN facility f ON f.id = df.id_facility
            JOIN department d ON d.id = df.id_department
            WHERE smf.status = 0
            AND smf.id_staff LIKE :idStaff
            """, nativeQuery = true)
        List<HOStaffMajorFacilityResponse> getMajorFacilities(String idStaff);

    @Query(value = """ 
                    SELECT d.id AS departmentId,
            	    CONCAT(d.code,' - ',d.name) AS departmentName
                    FROM department_facility df
                    JOIN department d ON d.id = df.id_department
                    WHERE df.status = 0
                    AND d.status = 0
                    AND df.id_facility LIKE :idFacility
            """, nativeQuery = true)
    List<HOStaffDepartmentResponse> getDepartmentByFacility(String idFacility);

    @Query(value = """ 
                   SELECT m.name AS majorName,
                          m.id AS majorId
                   FROM major_facility mf
                   JOIN department_facility df ON df.id = mf.id_department_facility
                   JOIN major m ON m.id = mf.id_major
                   WHERE m.status = 0
                   AND mf.status = 0
                   AND df.id_department LIKE :idDepartment
                   ANd df.id_facility LIKE :idFacility
            """, nativeQuery = true)
    List<HOStaffMajorResponse> getMajorByDepartmentFacility(String idDepartment, String idFacility);

    List<MajorFacility> findAllByDepartmentFacility_IdAndMajor_Code(String departmentFacilityId, String majorCode);

    List<MajorFacility> findAllByDepartmentFacility_IdAndMajor_IdAndStatus(String departmentFacilityId, String majorId, EntityStatus status);

}
