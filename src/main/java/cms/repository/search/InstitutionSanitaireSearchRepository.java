package cms.repository.search;

import cms.domain.InstitutionSanitaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstitutionSanitaire entity.
 */
public interface InstitutionSanitaireSearchRepository extends ElasticsearchRepository<InstitutionSanitaire, Long> {
}
