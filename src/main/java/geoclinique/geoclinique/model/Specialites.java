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
@Table(name = "specialites")
public class Specialites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSpecialite;
    @NotBlank
    @Size(max = 20)
    private String libelleSpecialite;
    @NotBlank
    @Size(max = 20)
    private String imageSpecialite;

    private String descriptionSpecialite;

    public Specialites(String libelleSpecialite, String descriptionSpecialite) {
        this.libelleSpecialite = libelleSpecialite;
        this.descriptionSpecialite = descriptionSpecialite;
    }

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "clinics")
//    private Clinique clinics;

    @JsonIgnore
    @ManyToMany(mappedBy = "listeSpecialiteCli")
    List<Clinique> listeClinique = new ArrayList<>();
    @JsonIgnore
    @ManyToMany(mappedBy = "listeSpecialiteMed")
    List<Medecins> listeMedecin = new ArrayList<>();


//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "medecins")
//    private Medecins medecins;
}
