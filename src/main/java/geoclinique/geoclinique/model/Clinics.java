package geoclinique.geoclinique.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

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
@Table(name = "clinics",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@SuperBuilder
@DynamicUpdate
@SelectBeforeUpdate
public class Clinics extends Utilisateur{
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private Long idClinic;
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

    public Clinics(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 50) @Email String email, @NotBlank @Size(max = 120) String password, String nomClinic, String descriptionClinic, String contactClinic, String villeClinic, String adresseClinic, String longitudeClinic, String latitudeClinic) {
        super(username, email, password);
        this.nomClinic = nomClinic;
        this.descriptionClinic = descriptionClinic;
        this.contactClinic = contactClinic;
        this.villeClinic = villeClinic;
        this.adresseClinic = adresseClinic;
        this.longitudeClinic = longitudeClinic;
        this.latitudeClinic = latitudeClinic;
    }

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
    @OneToMany(mappedBy = "clinics")
    List<Specialites> ListeSpecialite = new ArrayList<>();
}
