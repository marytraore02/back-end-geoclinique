package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.Clinique;
import geoclinique.geoclinique.model.Medecins;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedecinsRepository extends JpaRepository<Medecins, Long> {
    Optional<Medecins> findByNomMedecin(String nom);
    Optional<Medecins> findById(Long id);
//    Medecins findByIdClinique(Clinique clinique);
    boolean existsById(Long id);
    boolean existsByContactMedecin(String contact);
    boolean existsByEmailMedecin(String email);
    boolean existsByNomMedecin(String name);

}
