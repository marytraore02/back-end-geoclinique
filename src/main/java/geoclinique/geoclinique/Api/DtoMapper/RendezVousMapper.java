package geoclinique.geoclinique.Api.DtoMapper;

import geoclinique.geoclinique.Api.DtoViewModel.Response.RdvMedecinResponse;
import geoclinique.geoclinique.Api.DtoViewModel.Response.TodayRdvListDto;
import geoclinique.geoclinique.Api.DtoViewModel.Response.TodayRdvResponse;
import geoclinique.geoclinique.model.RendezVous;
import geoclinique.geoclinique.repository.CliniqueRepository;
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
    private CliniqueRepository clinicsRepository;


    // Afficher la liste total des RDV du jour
    public TodayRdvResponse toDto(List<RendezVous> rdvList, RendezVous rdv, int size){

        var TodayRendezVousResponse = new TodayRdvResponse();

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
    public TodayRdvListDto test(RendezVous rdv){

        var todayRendezVousListDto = new TodayRdvListDto();
        todayRendezVousListDto.setRdvId(rdv.getId());
        todayRendezVousListDto.setNomPatient(rdv.getPatients().getNomPatient());
        todayRendezVousListDto.setPrenomPatient(rdv.getPatients().getPrenomPatient());
        todayRendezVousListDto.setStatus(rdv.isActive());
        todayRendezVousListDto.setCalendrier_id(rdv.getCalendrier().getId());
        todayRendezVousListDto.setHeureDebut(rdv.getCalendrier().getHeureDebut());
        todayRendezVousListDto.setHeureFin(rdv.getCalendrier().getHeureFin());
        return todayRendezVousListDto;
    }


    // LE CALENDRIER DU MEDECIN RETOURNER
    public RdvMedecinResponse toRdvMedecinDto(RendezVous rdv){
        var RdvMedecin = new RdvMedecinResponse();
        RdvMedecin.setId(rdv.getCalendrier().getId());
        RdvMedecin.setCalendrier(rdv.getCalendrier().getHeureDebut() +" - "+rdv.getCalendrier().getHeureFin());
        RdvMedecin.setDisponible(false);
        return RdvMedecin;
    }

}
