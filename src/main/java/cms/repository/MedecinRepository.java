package cms.repository;

import cms.domain.Medecin;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Medecin entity.
 */
public interface MedecinRepository extends JpaRepository<Medecin,Long> {

}
