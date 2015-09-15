package cms.repository;

import cms.domain.Symptome;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Symptome entity.
 */
public interface SymptomeRepository extends JpaRepository<Symptome,Long> {

}
