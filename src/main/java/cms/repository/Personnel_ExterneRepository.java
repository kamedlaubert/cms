package cms.repository;

import cms.domain.Personnel_Externe;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Personnel_Externe entity.
 */
public interface Personnel_ExterneRepository extends JpaRepository<Personnel_Externe,Long> {

}
