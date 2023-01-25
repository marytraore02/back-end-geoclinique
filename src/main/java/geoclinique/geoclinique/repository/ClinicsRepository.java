package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.Clinics;
import geoclinique.geoclinique.model.Specialites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicsRepository extends JpaRepository<Clinics, Long> {
    Optional<Clinics>  findByNomClinic(String name);
    boolean existsByNomClinic(String name);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
