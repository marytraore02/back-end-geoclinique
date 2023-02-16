package geoclinique.geoclinique.service;

import geoclinique.geoclinique.model.Patients;
import geoclinique.geoclinique.model.RendezVous;
import geoclinique.geoclinique.model.Specialites;
import geoclinique.geoclinique.repository.RendezVousRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RendezVousService {
    @Autowired
    RendezVousRepository rendezVousRepository;

    public List<RendezVous> read(){
        return rendezVousRepository.findAll();
    }
//    public List<RendezVous> readRdvByStatus(){
//        return rendezVousRepository.findByIsActive();
//    }
    public Optional<RendezVous> GetOne(Long id){
        return rendezVousRepository.findById(id);
    }
    public Iterable<Object[]> getListRdv() {
        return rendezVousRepository.getListRdv();
    }
    public String delete(Long id){
        rendezVousRepository.deleteById(id);
        return "Supprimer avec succes";
    }
    public RendezVous modifier(Long id, RendezVous rendezVous) {
        RendezVous rdv = rendezVousRepository.findById(id).get();
        return rendezVousRepository.save(rdv);
//        return rendezVousRepository.findById(id)
//                .map(p->{
//                    p.setLibelleSpecialite(specialites.getLibelleSpecialite());
//                    p.setDescriptionSpecialite(specialites.getDescriptionSpecialite());
////                    p.setImageSpecialite(specialites.getImageSpecialite());
//                    return specialiteRepository.save(p);
//                }).orElseThrow(()-> new RuntimeException("Clinics non trouvé !"));
    }

}
