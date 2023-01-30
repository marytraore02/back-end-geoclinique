package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    //Liste de RDV de la clinic
    List<RendezVous> findAllByMedecins(Medecins medecins);

    //Liste de RDV du patient
    List<RendezVous> findAllByPatients(Patients patients);
    List<RendezVous> findAllByMedecinsAndDate(Medecins medecins, LocalDate date);
    Optional<RendezVous> findByMedecinsAndDateAndCalendrier(Medecins medecins, LocalDate date, Calendrier calendrier);

}
