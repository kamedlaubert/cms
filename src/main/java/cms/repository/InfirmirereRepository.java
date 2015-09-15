package cms.repository;

import cms.domain.Infirmirere;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Infirmirere entity.
 */
public interface InfirmirereRepository extends JpaRepository<Infirmirere,Long> {

}
