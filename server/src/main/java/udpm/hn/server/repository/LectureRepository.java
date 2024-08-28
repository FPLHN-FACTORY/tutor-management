package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Lecture;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, String> {
}
