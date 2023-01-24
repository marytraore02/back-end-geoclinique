package geoclinique.geoclinique.Api.DtoViewModel.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DisponibiliteClinicResponse {
    private Long id;
    private String calendrier;
    private boolean disponible;
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        final DisponibiliteClinicResponse other = (DisponibiliteClinicResponse) obj;

        if (this.getId().intValue() == other.getId().intValue()) {
            return true;
        }else{
            return false;
        }

    }
}
