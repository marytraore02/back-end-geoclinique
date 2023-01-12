package geoclinique.geoclinique.service;

import geoclinique.geoclinique.model.Specialites;
import geoclinique.geoclinique.repository.SpecialiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialiteService {
    @Autowired
    SpecialiteRepository specialiteRepository;

    public Specialites creer(Specialites specialites){
        return specialiteRepository.save(specialites);
    }
    public List<Specialites> read(){
        return specialiteRepository.findAll();
    }
    public Optional<Specialites> GetOne(Long id){
        return specialiteRepository.findById(id);
    }

    public boolean existsByName(String name){
        return specialiteRepository.existsByLibelleSpecialite(name);
    }
    public boolean existsById(Long id){
        return specialiteRepository.existsById(id);
    }

    public Optional<Specialites> getByLibelleSpecialite(String name){
        return specialiteRepository.findByLibelleSpecialite(name);
    }

    public Specialites modifier(Long id, Specialites specialites) {
        return specialiteRepository.findById(id)
                .map(p->{
                    p.setLibelleSpecialite(specialites.getLibelleSpecialite());
                    p.setDescriptionSpecialite(specialites.getDescriptionSpecialite());
//                    p.setImageSpecialite(specialites.getImageSpecialite());
                    return specialiteRepository.save(p);
                }).orElseThrow(()-> new RuntimeException("Clinics non trouvé !"));
    }

    public String delete(Long id){
         specialiteRepository.deleteById(id);
        return "Supprimer avec succes";
    }





}
