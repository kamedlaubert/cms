package cms.repository;

import cms.domain.Consultation;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Consultation entity.
 */
public interface ConsultationRepository extends JpaRepository<Consultation,Long> {

    @Query("select distinct consultation from Consultation consultation left join fetch consultation.conduires")
    List<Consultation> findAllWithEagerRelationships();

    @Query("select consultation from Consultation consultation left join fetch consultation.conduires where consultation.id =:id")
    Consultation findOneWithEagerRelationships(@Param("id") Long id);

}
