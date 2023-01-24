package geoclinique.geoclinique.util;

import geoclinique.geoclinique.Api.DtoViewModel.Response.DisponibiliteClinicResponse;
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

    public List<DisponibiliteClinicResponse> listAllDisponibiliteByStatus(List<DisponibiliteClinicResponse> disponibiliteClinicList){
        int shiftNb = this.calendrierRepository.findAll().size()+1;

        for(long i = 1 ; i < shiftNb; i++){
            Long j = Long.valueOf(i);
            var shift = this.calendrierRepository.getOne(j);
            DisponibiliteClinicResponse dummy = new DisponibiliteClinicResponse(shift.getId(), shift.getHeureDebut() +" - "+shift.getHeureFin(), true);
            if (!disponibiliteClinicList.contains(dummy))
            {
                disponibiliteClinicList.add(dummy);
            }
        }
//        return availabilityMedecinList;
        return disponibiliteClinicList.stream()
                .sorted(Comparator.comparing(DisponibiliteClinicResponse::getId))
                .collect(Collectors.toList());
    }

}
