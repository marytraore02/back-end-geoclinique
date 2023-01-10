package geoclinique.geoclinique.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "calendrier")
public class Calendrier {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idCalendrier;
    private String jourCalendrier;
    private String heureDebutCalendrier;
    private String heureFinCalendrier;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "medecins")
    private Medecins medecins;
}
