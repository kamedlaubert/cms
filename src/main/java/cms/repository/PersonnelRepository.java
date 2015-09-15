package cms.repository;

import cms.domain.Personnel;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Personnel entity.
 */
public interface PersonnelRepository extends JpaRepository<Personnel,Long> {

}
