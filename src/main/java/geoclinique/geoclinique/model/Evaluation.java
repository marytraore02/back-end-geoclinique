package geoclinique.geoclinique.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "evaluation")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idEvaluation;
    @Lob
    private String message;
    @ManyToOne()
    private RendezVous rendezVous;
    private LocalDate date;

    public Evaluation(String message, RendezVous rendezVous, LocalDate date) {
        this.message = message;
        this.rendezVous = rendezVous;
        this.date = date;
    }

    public Evaluation(RendezVous rendezVous, LocalDate date) {
        this.rendezVous = rendezVous;
        this.date = date;
    }

//    public Evaluation(Long idEvaluation, String messageEvaluation, Date date) {
//        this.idEvaluation = idEvaluation;
//        this.messageEvaluation = messageEvaluation;
//        this.date = date;
//    }

//    @JsonIgnore
//    @ManyToMany(mappedBy = "listeEvaluation")
//    List<RendezVous> listeRdv = new ArrayList<>();
}
