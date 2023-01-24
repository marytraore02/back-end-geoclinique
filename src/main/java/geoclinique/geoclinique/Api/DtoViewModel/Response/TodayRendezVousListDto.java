package geoclinique.geoclinique.Api.DtoViewModel.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TodayRendezVousListDto {
    private Long rdvId;
    private String patientName;
    private String patientEmail;
    private Long calendrier_id;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private boolean status;
}
