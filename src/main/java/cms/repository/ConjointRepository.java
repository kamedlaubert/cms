package cms.repository;

import cms.domain.Conjoint;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Conjoint entity.
 */
public interface ConjointRepository extends JpaRepository<Conjoint,Long> {

}
