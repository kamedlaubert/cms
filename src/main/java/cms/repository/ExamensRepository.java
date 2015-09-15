package cms.repository;

import cms.domain.Examens;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Examens entity.
 */
public interface ExamensRepository extends JpaRepository<Examens,Long> {

    @Query("select distinct examens from Examens examens left join fetch examens.consultations")
    List<Examens> findAllWithEagerRelationships();

    @Query("select examens from Examens examens left join fetch examens.consultations where examens.id =:id")
    Examens findOneWithEagerRelationships(@Param("id") Long id);

}
