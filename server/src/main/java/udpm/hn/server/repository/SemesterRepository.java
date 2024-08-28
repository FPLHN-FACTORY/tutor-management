package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Semester;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, String> {
}
