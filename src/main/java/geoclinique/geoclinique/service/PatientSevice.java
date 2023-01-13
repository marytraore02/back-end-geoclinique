package geoclinique.geoclinique.service;

import geoclinique.geoclinique.model.Clinics;
import geoclinique.geoclinique.model.Patients;
import geoclinique.geoclinique.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientSevice {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    PasswordEncoder encoder;

    public Patients creer(Patients patients){
        patients.setPassword(encoder.encode(patients.getPassword()));
        return patientRepository.save(patients);
    }
    public List<Patients> read(){
        return patientRepository.findAll();
    }
    public Optional<Patients> GetOne(Long id){
        return patientRepository.findById(id);
    }
    public Optional<Patients> getByEmail(String email){
        return patientRepository.findByEmail(email);
    }
    public boolean existsByConatct(String contact){
        return patientRepository.existsByContactPatient(contact);
    }
    public boolean existsByUsername(String username){
        return patientRepository.existsByUsername(username);
    }
    public boolean existsById(Long id){
        return patientRepository.existsById(id);
    }
    public boolean existsByEmail(String email){
        return patientRepository.existsByEmail(email);
    }
    public Patients modifier(Long id, Patients patients) {
        return patientRepository.findById(id)
                .map(p->{
                    p.setUsername(patients.getUsername());
                    p.setEmail(patients.getEmail());
                    p.setPassword(encoder.encode(patients.getPassword()));
                    p.setNomPatient(patients.getNomPatient());
                    p.setPrenomPatient(patients.getPrenomPatient());
                    p.setNaissancePatient(patients.getNaissancePatient());
                    p.setContactPatient(patients.getContactPatient());
                    p.setSexePatient(patients.getSexePatient());
                    return patientRepository.save(p);
                }).orElseThrow(()-> new RuntimeException("Clinics non trouvé !"));
    }
    public String delete(Long id){
        patientRepository.deleteById(id);
        return "Supprimer avec succes";
    }

}
