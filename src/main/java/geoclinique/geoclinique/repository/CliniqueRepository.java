package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.Clinique;
import geoclinique.geoclinique.model.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CliniqueRepository extends JpaRepository<Clinique, Long> {
//    Optional<Clinique> findByNomClinique(String name);
//    boolean existsByNomClinique(String name);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    @Query(value = "SELECT * FROM `clinique` WHERE clinique.status_clinique=1", nativeQuery = true)
    Iterable<List<Clinique>> getListCliniqueValide();

}
