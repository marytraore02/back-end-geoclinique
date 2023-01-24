package geoclinique.geoclinique.Api.DtoViewModel.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisponibiliteClinicRequest {
    private Long clinicId;
    private String date;
}
