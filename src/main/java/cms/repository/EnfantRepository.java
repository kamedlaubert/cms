package cms.repository;

import cms.domain.Enfant;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Enfant entity.
 */
public interface EnfantRepository extends JpaRepository<Enfant,Long> {

}
