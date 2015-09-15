package cms.repository.search;

import cms.domain.Consultation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Consultation entity.
 */
public interface ConsultationSearchRepository extends ElasticsearchRepository<Consultation, Long> {
}
