package geoclinique.geoclinique.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class PatientRequest {
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
    private String sexePatient;
    //private String image;
}
