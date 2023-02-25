package geoclinique.geoclinique.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String message;
    private String nom;
    private String prenom;
    private String emailPatient;
    LocalTime heureDebut;
    LocalTime heureFin;
    private LocalDate date;
    boolean status;

    public Messages(String message, String nom, String prenom, String emailPatient, LocalTime heureDebut, LocalTime heureFin, LocalDate date, boolean status) {
        this.message = message;
        this.nom = nom;
        this.prenom = prenom;
        this.emailPatient = emailPatient;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.date = date;
        this.status = status;
    }
}
