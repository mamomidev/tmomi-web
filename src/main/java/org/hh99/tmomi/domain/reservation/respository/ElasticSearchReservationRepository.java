package org.hh99.tmomi.domain.reservation.respository;

import java.util.List;

import org.hh99.tmomi.domain.reservation.Status;
import org.hh99.tmomi.domain.reservation.document.ElasticSearchReservation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticSearchReservationRepository extends ElasticsearchRepository<ElasticSearchReservation, String> {
	List<ElasticSearchReservation> findAllByEventTimesId(Long eventTimeId);
}
