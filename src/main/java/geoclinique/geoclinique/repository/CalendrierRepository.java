package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.Calendrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendrierRepository extends JpaRepository<Calendrier, Long> {
    boolean existsById(Long id);
}
