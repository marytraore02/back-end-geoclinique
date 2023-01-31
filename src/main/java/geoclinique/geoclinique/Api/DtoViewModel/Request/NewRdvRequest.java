package geoclinique.geoclinique.Api.DtoViewModel.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewRdvRequest {
    private String nom;
    private String prenom;
    private String email;
    private String date;
    private Long calendrierId;
    private Long medecinId;
    private boolean isValide = false;
}
