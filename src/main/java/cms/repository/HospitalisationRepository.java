package cms.repository;

import cms.domain.Hospitalisation;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Hospitalisation entity.
 */
public interface HospitalisationRepository extends JpaRepository<Hospitalisation,Long> {

}
