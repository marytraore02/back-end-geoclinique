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
@Table(name = "medecins")
public class Medecins {
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
    @Size(max = 120)
    private String passwordMedecin;
    @NotBlank
    @Size(max = 20)
    private String sexeMedecin;
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
    @OneToMany()
    List<Specialites> ListeSpecMedecins = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "medecins")
    List<Calendrier> ListeCalendrier = new ArrayList<>();
}
