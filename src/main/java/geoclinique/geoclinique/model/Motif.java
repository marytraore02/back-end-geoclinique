package geoclinique.geoclinique.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "motif")
public class Motif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idModif;
    @NotBlank
    @Size(max = 50)
    private String libelleMotif;

    public Motif(Long idModif, String libelleMotif) {
        this.idModif = idModif;
        this.libelleMotif = libelleMotif;
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "motif")
    List<RendezVous> ListeRdv = new ArrayList<>();


}
