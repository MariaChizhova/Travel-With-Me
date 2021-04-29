package server.repositories;

import server.models.Marker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkerRepository extends CrudRepository<Marker, Long> {
}
