package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.Clinics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicsRepository extends JpaRepository<Clinics, Long> {
//    Optional<Clinics> findByUsername(String username);

    Boolean existsBynomClinic(String name);

//    Boolean existsByEmail(String email);
}
