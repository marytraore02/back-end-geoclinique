package geoclinique.geoclinique.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "medecins")
public class Medecins implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idMedecin;
    @NotBlank
    @Size(max = 20)
    private String nomMedecin;
    @NotBlank
    @Size(max = 20)
    private String prenomMedecin;
    @NotBlank
    @Size(max = 50)
    @Email
    private String emailMedecin;
    @NotBlank
    @Size(max = 20)
    private String sexeMedecin;
    private Date naissanceMedecin;
    @NotBlank
    @Size(max = 20)
    private String imageMedecin;
    @NotBlank
    @Size(max = 20)
    private String contactMedecin;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_clinics")
    private Clinics clinics;

    @JsonIgnore
    @OneToMany(mappedBy = "medecins")
    List<Specialites> ListeSpecMedecins = new ArrayList<>();

    public Medecins(String nomMedecin, String prenomMedecin, String emailMedecin, String sexeMedecin, Date naissanceMedecin, String imageMedecin, String contactMedecin, Clinics clinics) {
        this.nomMedecin = nomMedecin;
        this.prenomMedecin = prenomMedecin;
        this.emailMedecin = emailMedecin;
        this.sexeMedecin = sexeMedecin;
        this.naissanceMedecin = naissanceMedecin;
        this.imageMedecin = imageMedecin;
        this.contactMedecin = contactMedecin;
       // this.utilisateur = utilisateur;
        this.clinics = clinics;
    }
}
