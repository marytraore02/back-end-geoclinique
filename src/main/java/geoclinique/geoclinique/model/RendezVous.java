package geoclinique.geoclinique.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "rdv")
public class RendezVous implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "clinic_id", nullable = true)
//    private Clinique clinics;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medecin_id", nullable = true)
    private Medecins medecins;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patients patients;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendrier_id", nullable = false)
    private Calendrier calendrier;

    private LocalDate date;

    private boolean isActive;

    public RendezVous() {
    }

    public RendezVous(Medecins medecins, Patients patients, Calendrier calendrier, LocalDate date, boolean isActive) {
        this.medecins = medecins;
        this.patients = patients;
        this.calendrier = calendrier;
        this.date = date;
        this.isActive = isActive;
    }
}
