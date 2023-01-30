package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.Clinique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CliniqueRepository extends JpaRepository<Clinique, Long> {
    Optional<Clinique> findByNomClinique(String name);
    boolean existsByNomClinique(String name);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
