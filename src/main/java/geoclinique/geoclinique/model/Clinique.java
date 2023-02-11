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
@Table(name = "clinique",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@SuperBuilder
@DynamicUpdate
@SelectBeforeUpdate
public class Clinique extends Utilisateur{
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private Long idClinic;
//    @NotBlank
//    @Size(max = 20)
//    private String nomClinique;
    @NotBlank
    @Size(max = 500)
    private String descriptionClinique;
//    @NotBlank
//    @Size(max = 20)
//    private String contactClinique;
    @NotBlank
    @Size(max = 20)
    private String villeClinique;
    @NotBlank
    @Size(max = 50)
    private String adresseClinique;
    @NotBlank
    @Size(max = 50)
    private String longitudeClinique;
    @NotBlank
    @Size(max = 50)
    private String latitudeClinique;
    @NotBlank
    @Size(max = 20)
    private String agrementClinique;
    private boolean statusClinique;



    public Clinique(@NotBlank @Size(max = 20) String nomEtPrenom, @NotBlank @Size(max = 20) String contact, @NotBlank @Size(max = 20) String date, @NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 50) @Email String email, @NotBlank @Size(max = 120) String password, String descriptionClinique, String villeClinique, String adresseClinique, String longitudeClinique, String latitudeClinique) {
        super(nomEtPrenom, contact, date, username, email, password);
        this.descriptionClinique = descriptionClinique;
        this.villeClinique = villeClinique;
        this.adresseClinique = adresseClinique;
        this.longitudeClinique = longitudeClinique;
        this.latitudeClinique = latitudeClinique;
    }

//    public Clinique(@NotBlank @Size(max = 20) String nomEtPrenom, @NotBlank @Size(max = 20) String contact, @NotBlank @Size(max = 20) String date, @NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 100) @Email String email, @NotBlank @Size(max = 120) String password, String descriptionClinique, String villeClinique, String adresseClinique, String longitudeClinique, String latitudeClinique, List<Specialites> listeSpecialiteCli) {
//        super(nomEtPrenom, contact, date, username, email, password);
//        this.descriptionClinique = descriptionClinique;
//        this.villeClinique = villeClinique;
//        this.adresseClinique = adresseClinique;
//        this.longitudeClinique = longitudeClinique;
//        this.latitudeClinique = latitudeClinique;
//        this.listeSpecialiteCli = listeSpecialiteCli;
//    }


    public Clinique(@NotBlank @Size(max = 20) String nomEtPrenom, @NotBlank @Size(max = 20) String contact, @NotBlank @Size(max = 20) String date, @NotBlank @Size(max = 200) String image, @NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 100) @Email String email, @NotBlank @Size(max = 120) String password, Set<Role> roles, String descriptionClinique, String villeClinique, String adresseClinique, String longitudeClinique, String latitudeClinique) {
        super(nomEtPrenom, contact, date, image, username, email, password, roles);
        this.descriptionClinique = descriptionClinique;
        this.villeClinique = villeClinique;
        this.adresseClinique = adresseClinique;
        this.longitudeClinique = longitudeClinique;
        this.latitudeClinique = latitudeClinique;
    }

    public Clinique(@NotBlank @Size(max = 20) String nomEtPrenom, @NotBlank @Size(max = 20) String contact, @NotBlank @Size(max = 20) String date, @NotBlank @Size(max = 200) String image, @NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 100) @Email String email, @NotBlank @Size(max = 120) String password, String descriptionClinique, String villeClinique, String adresseClinique, String longitudeClinique, String latitudeClinique, List<Specialites> listeSpecialiteCli) {
        super(nomEtPrenom, contact, date, image, username, email, password);
        this.descriptionClinique = descriptionClinique;
        this.villeClinique = villeClinique;
        this.adresseClinique = adresseClinique;
        this.longitudeClinique = longitudeClinique;
        this.latitudeClinique = latitudeClinique;
        this.listeSpecialiteCli = listeSpecialiteCli;
    }
//Liste des RDV
//    @JsonIgnore
//    @OneToMany(cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY,
//            mappedBy = "clinics")
//    List<RendezVous> ListeRdv = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy = "clinique")
    List<Medecins> listeMedecins = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "clinics")
    List<Messages> ListeMessages = new ArrayList<>();

//    @JsonIgnore
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "rdv", joinColumns = { @JoinColumn(name = "id_clinics") }, inverseJoinColumns = {
//            @JoinColumn(name = "id_patients") })
//    List<Patients> listePatients = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "evaluation", joinColumns = { @JoinColumn(name = "id_clinics") }, inverseJoinColumns = {
            @JoinColumn(name = "id_patients") })
    List<Patients> listeEvaluation = new ArrayList<>();


    //@JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "specialite_clinique", joinColumns = { @JoinColumn(name = "id_clinics") }, inverseJoinColumns = {
            @JoinColumn(name = "id_specialite") })
    List<Specialites> listeSpecialiteCli = new ArrayList<>();

//    @JsonIgnore
//    @OneToMany(mappedBy = "clinics")
//    List<Specialites> ListeSpecialite = new ArrayList<>();
}
