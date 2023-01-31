package geoclinique.geoclinique.util;

import geoclinique.geoclinique.Api.DtoViewModel.Response.RdvMedecinResponse;
import geoclinique.geoclinique.repository.CalendrierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TweakResponse {

    @Autowired
    CalendrierRepository calendrierRepository;

    public List<RdvMedecinResponse> listAllRdvByStatus(List<RdvMedecinResponse> rdvMedecinList){

        // LA VARIABLE QUI VA RETOURNER LE NOMBRE TOTAL DES HORAIRES
        int shiftNb = this.calendrierRepository.findAll().size()+1;
        System.err.println(shiftNb);

        // ON PARCOURS LA LISTE
        for(long i = 1 ; i < shiftNb; i++){
            Long j = Long.valueOf(i);
            // shift PREND CHAQUE CHAMP DE LA LISTE DE MANIERE UNIQUE
            var shift = this.calendrierRepository.getOne(j);
            System.out.print(shift);

            // L'OBJET dummy PREND LES DISPONIBILITES VALIDE
            RdvMedecinResponse dummy = new RdvMedecinResponse(shift.getId(), shift.getHeureDebut() +" - "+shift.getHeureFin(), true);
            if (!rdvMedecinList.contains(dummy))
            {
                rdvMedecinList.add(dummy);
            }
        }
        //return rdvMedecinList;
        return rdvMedecinList.stream()
                .sorted(Comparator.comparing(RdvMedecinResponse::getId))
                .collect(Collectors.toList());
    }

}
