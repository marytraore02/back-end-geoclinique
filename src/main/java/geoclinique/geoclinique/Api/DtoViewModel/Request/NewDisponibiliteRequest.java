package geoclinique.geoclinique.Api.DtoViewModel.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewDisponibiliteRequest {
    private String nom;
    private String prenom;
    private String date;
    private Long calendrierId;
    private Long clinicId;
    private boolean isValide = false;
}
