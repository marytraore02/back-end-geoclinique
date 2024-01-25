package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.Medecins;
import geoclinique.geoclinique.model.Motif;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MotifRepository extends JpaRepository<Motif, Long> {
    boolean existsById(Long id);
    Optional<Motif> findById(Long id);

}
