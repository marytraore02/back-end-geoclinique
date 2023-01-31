package geoclinique.geoclinique.Api.DtoViewModel.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodayRdvRequest {
    private Long medecinId;
    private String date;
}
