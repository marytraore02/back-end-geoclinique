package geoclinique.geoclinique.Api.DtoMapper;

import geoclinique.geoclinique.Api.DtoViewModel.Response.DisponibiliteClinicResponse;
import geoclinique.geoclinique.Api.DtoViewModel.Response.TodayRendezVousListDto;
import geoclinique.geoclinique.Api.DtoViewModel.Response.TodayRendezVousResponse;
import geoclinique.geoclinique.model.RendezVous;
import geoclinique.geoclinique.repository.ClinicsRepository;
import geoclinique.geoclinique.repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RendezVousMapper {
    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private ClinicsRepository clinicsRepository;


    // Afficher des horaires du jour
    public TodayRendezVousResponse toDto(List<RendezVous> rdvList, RendezVous rdv, int size){

        var TodayRendezVousResponse = new TodayRendezVousResponse();

        TodayRendezVousResponse.setDate(rdv.getDate());
        TodayRendezVousResponse.setSize(size);
        TodayRendezVousResponse.setEvents(
                rdvList.stream()
                        .sorted((a1, a2) -> {
                            System.out.printf("sort: %s; %s\n", a1, a2);
                            return a1.getCalendrier().getId().compareTo(a2.getCalendrier().getId());
                        })
                        .map(a -> this.test(a))

                        .collect(Collectors.toList())
        );

        return TodayRendezVousResponse;
    }


    // Liste des RDV du jour
    public TodayRendezVousListDto test(RendezVous rdv){

        var todayRendezVousListDto = new TodayRendezVousListDto();
        todayRendezVousListDto.setRdvId(rdv.getId());
        todayRendezVousListDto.setPatientEmail(rdv.getPatients().getEmail());
        todayRendezVousListDto.setPatientName(rdv.getPatients().getPrenomPatient());
        todayRendezVousListDto.setStatus(rdv.isActive());
        todayRendezVousListDto.setCalendrier_id(rdv.getCalendrier().getId());
        todayRendezVousListDto.setHeureDebut(rdv.getCalendrier().getHeureDebut());
        todayRendezVousListDto.setHeureFin(rdv.getCalendrier().getHeureFin());
        return todayRendezVousListDto;
    }


    public DisponibiliteClinicResponse toDisponibiliteClinicDto(RendezVous rdv){
        var disponibiliteClinic = new DisponibiliteClinicResponse();
        disponibiliteClinic.setId(rdv.getCalendrier().getId());
        disponibiliteClinic.setCalendrier(rdv.getCalendrier().getHeureDebut() +" - "+rdv.getCalendrier().getHeureFin());
        disponibiliteClinic.setDisponible(false);
        return disponibiliteClinic;
    }

}
