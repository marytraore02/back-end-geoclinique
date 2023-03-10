package geoclinique.geoclinique.Api.DtoViewModel.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DisponibiliteMedecinResponse {
    private Long id;
    private String calendrier;
    private boolean disponible;
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        final DisponibiliteMedecinResponse other = (DisponibiliteMedecinResponse) obj;

        if (this.getId().intValue() == other.getId().intValue()) {
            return true;
        }else{
            return false;
        }

    }
}
