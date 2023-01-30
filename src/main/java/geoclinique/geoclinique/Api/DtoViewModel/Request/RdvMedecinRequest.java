package geoclinique.geoclinique.Api.DtoViewModel.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdvMedecinRequest {
    private Long medecinId;
    private String date;
}
