package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.Motif;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotifRepository extends JpaRepository<Motif, Long> {
    boolean existsById(Long id);
}
