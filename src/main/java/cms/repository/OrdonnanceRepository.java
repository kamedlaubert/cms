package cms.repository;

import cms.domain.Ordonnance;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ordonnance entity.
 */
public interface OrdonnanceRepository extends JpaRepository<Ordonnance,Long> {

}
