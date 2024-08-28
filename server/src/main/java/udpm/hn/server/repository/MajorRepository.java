package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Major;

@Repository
public interface MajorRepository extends JpaRepository<Major, String> {
}
