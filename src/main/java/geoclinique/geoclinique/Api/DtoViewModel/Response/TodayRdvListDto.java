package geoclinique.geoclinique.Api.DtoViewModel.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TodayRdvListDto {
    private Long rdvId;
    private String NomPatient;
    private String PrenomPatient;
    private Long calendrier_id;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private boolean status;
}
