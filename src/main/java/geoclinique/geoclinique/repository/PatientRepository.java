package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.Patients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patients, Long> {
    //Optional<Patients> findByEmail(String name);
    Patients findByEmail(String email);
//    boolean existsByContactPatient(String contact);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);


}
