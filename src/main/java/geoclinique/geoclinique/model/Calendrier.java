package geoclinique.geoclinique.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "calendrier")
public class Calendrier {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    //Liste des RDV
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "calendrier")
    List<RendezVous> ListeRdv = new ArrayList<>();

    public Calendrier(Long id, LocalTime heureDebut, LocalTime heureFin) {
        this.id = id;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }
}
