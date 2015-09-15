package cms.repository;

import cms.domain.BPC;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BPC entity.
 */
public interface BPCRepository extends JpaRepository<BPC,Long> {

}
