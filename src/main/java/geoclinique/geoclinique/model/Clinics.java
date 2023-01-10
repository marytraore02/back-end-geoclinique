package geoclinique.geoclinique.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "clinics")
//@SuperBuilder
//@DynamicUpdate
//@SelectBeforeUpdate
public class Clinics {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idClinic;
    @NotBlank
    @Size(max = 20)
    private String nomClinic;
    @NotBlank
    @Size(max = 500)
    private String descriptionClinic;
    @NotBlank
    @Size(max = 20)
    private String contactClinic;
    @NotBlank
    @Size(max = 20)
    private String villeClinic;
    @NotBlank
    @Size(max = 50)
    private String adresseClinic;
    @NotBlank
    @Size(max = 50)
    private String longitudeClinic;
    @NotBlank
    @Size(max = 50)
    private String latitudeClinic;
    @NotBlank
    @Size(max = 20)
    private String agrementClinic;
    private boolean statusClinic;

    @JsonIgnore
    @OneToMany(mappedBy = "clinics")
    List<Medecins> listeMedecins = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "clinics")
    List<Messages> ListeMessages = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rdv", joinColumns = { @JoinColumn(name = "id_clinics") }, inverseJoinColumns = {
            @JoinColumn(name = "id_patients") })
    List<Patients> listePatients = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "evaluation", joinColumns = { @JoinColumn(name = "id_clinics") }, inverseJoinColumns = {
            @JoinColumn(name = "id_patients") })
    List<Patients> listeEvaluation = new ArrayList<>();

    @JsonIgnore
    @OneToMany()
    List<Specialites> ListeSpecialite = new ArrayList<>();
}