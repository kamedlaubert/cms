package cms.repository;

import cms.domain.InstitutionSanitaire;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstitutionSanitaire entity.
 */
public interface InstitutionSanitaireRepository extends JpaRepository<InstitutionSanitaire,Long> {

}
