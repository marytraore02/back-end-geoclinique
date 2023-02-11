package geoclinique.geoclinique.payload.request;

import geoclinique.geoclinique.model.Specialites;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;


@Getter
@Setter
public class CliniqueRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    @Column(unique = true)
    private String nomEtPrenom;
    @NotBlank
    @Size(max = 20)
    private String contact;
    @NotBlank
    @Size(max = 20)
    private String date;
    @NotBlank
    @Size(max = 200)
    private String image;
    @NotBlank
    @Size(max = 20)
    private String username;
    @NotBlank
    @Size(max = 100)
    @Email
    private String email;
    @NotBlank
    @Size(max = 120)
    private String password;
    private Set<String> role;
    @NotBlank
    @Size(max = 500)
    private String descriptionClinique;
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

    List<Specialites> listSpecialiteClinique = new ArrayList<>();


}
