package geoclinique.geoclinique.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class PatientRequest {
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
    private String nomPatient;
    @NotBlank
    @Size(max = 20)
    private String prenomPatient;
    @NotBlank
    @Size(max = 20)
    private String sexePatient;
    @NotBlank
    @Size(max = 20)
    private String naissancePatient;
    @NotBlank
    @Size(max = 20)
    private String contactPatient;
    //private String image;
}
