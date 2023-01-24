package geoclinique.geoclinique.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private String messageEvaluation;
    private LocalDate date;


    @JsonIgnore
    @ManyToMany(mappedBy = "listeEvaluation")
    List<Clinics> listeClinics = new ArrayList<>();
}
