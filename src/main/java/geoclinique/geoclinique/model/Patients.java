package geoclinique.geoclinique.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "patients",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@SuperBuilder
@AllArgsConstructor
public class Patients extends Utilisateur{
    @NotBlank
    @Size(max = 20)
    private String nomPatient;
    @NotBlank
    @Size(max = 20)
    private String prenomPatient;
    @NotBlank
    @Size(max = 20)
    private String sexePatient;
    @NotBlank
    @Size(max = 20)
    private String naissancePatient;
    @NotBlank
    @Size(max = 20)
    private String contactPatient;

    //private String image;


    public Patients(String nomPatient, String prenomPatient) {
        this.nomPatient = nomPatient;
        this.prenomPatient = prenomPatient;
    }

    public Patients(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 50) @Email String email, @NotBlank @Size(max = 120) String password, String nomPatient, String prenomPatient, String sexePatient, String naissancePatient, String contactPatient) {
        super(username, email, password);
        this.nomPatient = nomPatient;
        this.prenomPatient = prenomPatient;
        this.sexePatient = sexePatient;
        this.naissancePatient = naissancePatient;
        this.contactPatient = contactPatient;
    }

//    public Patients(UtilisateurBuilder<?, ?> email, String nomPatient, String prenomPatient) {
//        super(email);
//        this.nomPatient = nomPatient;
//        this.prenomPatient = prenomPatient;
//    }

    //Liste des RDV
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "patients")
    List<RendezVous> ListeRdv = new ArrayList<>();

//    @JsonIgnore
//    @ManyToMany(mappedBy = "listePatients")
//    List<Clinics> listeClinics = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "patients")
    List<Messages> ListeMesagePatient = new ArrayList<>();

}
