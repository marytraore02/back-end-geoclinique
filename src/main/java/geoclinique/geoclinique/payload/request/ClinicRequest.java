package geoclinique.geoclinique.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class ClinicRequest {
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

}
