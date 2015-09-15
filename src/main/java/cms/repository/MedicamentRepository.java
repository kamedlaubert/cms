package cms.repository;

import cms.domain.Medicament;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Medicament entity.
 */
public interface MedicamentRepository extends JpaRepository<Medicament,Long> {

    @Query("select distinct medicament from Medicament medicament left join fetch medicament.ordonnances")
    List<Medicament> findAllWithEagerRelationships();

    @Query("select medicament from Medicament medicament left join fetch medicament.ordonnances where medicament.id =:id")
    Medicament findOneWithEagerRelationships(@Param("id") Long id);

}
