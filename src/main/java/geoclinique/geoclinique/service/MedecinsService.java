package geoclinique.geoclinique.service;

import geoclinique.geoclinique.model.Clinique;
import geoclinique.geoclinique.model.Medecins;
import geoclinique.geoclinique.model.Motif;
import geoclinique.geoclinique.repository.MedecinsRepository;
import geoclinique.geoclinique.repository.MotifRepository;
import geoclinique.geoclinique.repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedecinsService {
    @Autowired
    MedecinsRepository medecinsRepository;
    @Autowired
    MotifRepository motifRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    RendezVousRepository rendezVousRepository;
    public Medecins creer(Medecins medecins){
        return medecinsRepository.save(medecins);
    }
    public List<Medecins> read(){
        return medecinsRepository.findAll();
    }

    public Medecins getOne(Long id){
        return medecinsRepository.findById(id).get();
    }
//    public Medecins getByClinique(Clinique clinique){
//        return medecinsRepository.findByIdClinique(clinique);
//    }
//    public Optional<Medecins> GetOne(Long id){
//        return medecinsRepository.findById(id);
//    }
    public boolean existsByName(String name){
        return medecinsRepository.existsByNomMedecin(name);
    }
    public boolean existeByEmailMedecin(String email){
        return medecinsRepository.existsByEmailMedecin(email);
    }

    public boolean existByContact(String contact){
        return medecinsRepository.existsByContactMedecin(contact);
    }
    public boolean existsById(Long id){
        return medecinsRepository.existsById(id);
    }
    public Optional<Medecins> getByNomMedecin(String nom){
        return medecinsRepository.findByNomMedecin(nom);
    }
    public Medecins modifier(Long id, Medecins medecins) {
//        Medecins dt = medecinsRepository.findById(id).get();
//            return medecinsRepository.save(dt);

        return  medecinsRepository.findById(id)
                .map(p->{
                    p.setNomMedecin(medecins.getNomMedecin());
                    p.setPrenomMedecin(medecins.getPrenomMedecin());
                    p.setEmailMedecin(medecins.getEmailMedecin());
                    p.setNaissanceMedecin(medecins.getNaissanceMedecin());
                    p.setSexeMedecin(medecins.getSexeMedecin());
                    p.setContactMedecin(medecins.getContactMedecin());
                    return medecinsRepository.save(p);
                }).orElseThrow(()-> new RuntimeException("Medecin non trouvé !"));
    }
    public String delete(Long id){
        medecinsRepository.deleteById(id);
        return "Supprimer avec succes";
    }







}
