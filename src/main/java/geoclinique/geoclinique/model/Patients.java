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
import java.util.Date;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "patients")
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
    private Date naissancePatient;
    @NotBlank
    @Size(max = 20)
    private String contactPatient;

    private String image;

    public Patients(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 50) @Email String email, @NotBlank @Size(max = 120) String password, String nomPatient, String prenomPatient, String sexePatient, Date naissancePatient, String contactPatient) {
        super(username, email, password);
        this.nomPatient = nomPatient;
        this.prenomPatient = prenomPatient;
        this.sexePatient = sexePatient;
        this.naissancePatient = naissancePatient;
        this.contactPatient = contactPatient;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "listePatients")
    List<Clinics> listeClinics = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "patients")
    List<Messages> ListeMesagePatient = new ArrayList<>();
}
