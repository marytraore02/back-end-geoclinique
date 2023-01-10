package geoclinique.geoclinique.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "patients")
public class Patients {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idPatient;
    @NotBlank
    @Size(max = 20)
    private String nomPatient;
    @NotBlank
    @Size(max = 20)
    private String prenomPatient;
    @NotBlank
    @Size(max = 50)
    @Email
    private String emailPatient;
    @NotBlank
    @Size(max = 120)
    private String passwordPatient;
    @NotBlank
    @Size(max = 20)
    private String sexePatient;
    @NotBlank
    @Size(max = 20)
    private String naissancePatient;
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
