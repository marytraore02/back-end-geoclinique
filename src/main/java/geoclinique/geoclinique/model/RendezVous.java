package geoclinique.geoclinique.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = true)
    private Clinics clinics;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patients patients;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_calendrier", nullable = false)
    private Calendrier calendrier;

    private LocalDate date;

    private boolean isActive;

    public RendezVous() {
    }

    public RendezVous(Clinics clinics, Patients patients, Calendrier calendrier, LocalDate date, boolean isActive) {
        this.clinics = clinics;
        this.patients = patients;
        this.calendrier = calendrier;
        this.date = date;
        this.isActive = isActive;
    }
}
