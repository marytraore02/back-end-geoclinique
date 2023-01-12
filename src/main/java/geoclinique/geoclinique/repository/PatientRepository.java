package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.Clinics;
import geoclinique.geoclinique.model.Patients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patients, Long> {
//    Optional<Patients> findByEmail(String name);
    boolean existsByContactPatient(String contact);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);


}
