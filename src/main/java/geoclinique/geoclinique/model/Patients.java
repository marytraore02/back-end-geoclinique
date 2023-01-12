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

    @JsonIgnore
    @ManyToMany(mappedBy = "listePatients")
    List<Clinics> listeClinics = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "patients")
    List<Messages> ListeMesagePatient = new ArrayList<>();
}
