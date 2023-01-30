package geoclinique.geoclinique.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class CliniqueRequest {
    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;
    private Set<String> role;
    @NotBlank
    @Size(max = 20)
    private String nomClinique;
    @NotBlank
    @Size(max = 500)
    private String descriptionClinique;
    @NotBlank
    @Size(max = 20)
    private String contactClinique;
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


}
