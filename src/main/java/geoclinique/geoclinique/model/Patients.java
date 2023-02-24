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
    private String sexePatient;
    public Patients(@NotBlank @Size(max = 20) String nomEtPrenom, @NotBlank @Size(max = 20) String contact, @NotBlank @Size(max = 20) String date, @NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 50) @Email String email, @NotBlank @Size(max = 120) String password, String sexePatient) {
        super(nomEtPrenom, contact, date, username, email, password);
        this.sexePatient = sexePatient;
    }

    public Patients(@NotBlank @Size(max = 20) String nomEtPrenom, @NotBlank @Size(max = 20) String contact, @NotBlank @Size(max = 20) String date, @NotBlank @Size(max = 200) String image, @NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 100) @Email String email, @NotBlank @Size(max = 120) String password, String sexePatient) {
        super(nomEtPrenom, contact, date, image, username, email, password);
        this.sexePatient = sexePatient;
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

//    @JsonIgnore
//    @OneToMany(mappedBy = "patients")
//    List<FeedBack> ListeMesagePatient = new ArrayList<>();

}
