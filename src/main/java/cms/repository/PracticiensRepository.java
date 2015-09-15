package cms.repository;

import cms.domain.Practiciens;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Practiciens entity.
 */
public interface PracticiensRepository extends JpaRepository<Practiciens,Long> {

}
