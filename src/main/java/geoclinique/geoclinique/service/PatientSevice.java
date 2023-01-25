package geoclinique.geoclinique.service;

import geoclinique.geoclinique.Api.DtoMapper.RendezVousMapper;
import geoclinique.geoclinique.Api.DtoViewModel.Request.DisponibiliteClinicRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Request.NewDisponibiliteRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Response.ApiResponse;
import geoclinique.geoclinique.Api.DtoViewModel.Response.DisponibiliteClinicResponse;
import geoclinique.geoclinique.model.Clinics;
import geoclinique.geoclinique.model.Patients;
import geoclinique.geoclinique.model.RendezVous;
import geoclinique.geoclinique.repository.CalendrierRepository;
import geoclinique.geoclinique.repository.ClinicsRepository;
import geoclinique.geoclinique.repository.PatientRepository;
import geoclinique.geoclinique.repository.RendezVousRepository;
import geoclinique.geoclinique.util.TweakResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientSevice {
    @Autowired
    ClinicsRepository clinicsRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    RendezVousRepository rendezVousRepository;
    @Autowired
    CalendrierRepository calendrierRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    RendezVousMapper rendezVousMapper;
    private TweakResponse tweakResponse;

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
    public Patients getByEmail( String email){
        return patientRepository.findByEmail(email);
    }
//    public Optional<Patients> getByEmail(String email){
//        return patientRepository.findByEmail(email);
//    }
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
    public Patients modifier(Patients patients) {
//        return patientRepository.findById(id)
//                .map(p->{
//                    p.setUsername(patients.getUsername());
//                    p.setEmail(patients.getEmail());
//                    p.setPassword(encoder.encode(patients.getPassword()));
//                    p.setNomPatient(patients.getNomPatient());
//                    p.setPrenomPatient(patients.getPrenomPatient());
//                    p.setNaissancePatient(patients.getNaissancePatient());
//                    p.setContactPatient(patients.getContactPatient());
//                    p.setSexePatient(patients.getSexePatient());
//                    return patientRepository.save(p);
//                }).orElseThrow(()-> new RuntimeException("Clinics non trouvé !"));
        // TODO Auto-generated method stub

        return patientRepository.save(patients);

    }
    public String delete(Long id){
        patientRepository.deleteById(id);
        return "Supprimer avec succes";
    }


    //List de disponibilite d'une clinic par jour
    public List<DisponibiliteClinicResponse> listAllClinicDisponible(DisponibiliteClinicRequest clinicDisponible){

        var clinic = this.clinicsRepository.findById(clinicDisponible.getClinicId());
        var date = LocalDate.parse(clinicDisponible.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        if(clinic.isEmpty()){
            return null;
        }
        var clinicDisponibles = this.rendezVousRepository.findAllByClinicsAndDate(clinic.get(), date);

        var clinicDisponibleList =
                clinicDisponibles.stream().sorted((a1, a2) -> {
                            System.out.printf("sort: %s; %s\n", a1, a2);
                            return a1.getCalendrier().getId().compareTo(a2.getCalendrier().getId());
                        })
                        .map(a -> this.rendezVousMapper.toDisponibiliteClinicDto(a))
                        .collect(Collectors.toList());

        return this.tweakResponse.listAllDisponibiliteByStatus(clinicDisponibleList);
    }


    // Ajouter une RDV
    public Object save(NewDisponibiliteRequest newDisponibilite){

        var clinic = this.clinicsRepository.findById(newDisponibilite.getClinicId());
        var calendrier = this.calendrierRepository.findById(newDisponibilite.getCalendrierId());
        var date = LocalDate.parse(newDisponibilite.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        if(clinic.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Clinic non trouvé."),
                    HttpStatus.NOT_FOUND);
        }
        if(calendrier.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"calendar does not exist, please try again."),
                    HttpStatus.BAD_REQUEST);
        }

        var isAvailable = this.rendezVousRepository.findByClinicsAndDateAndCalendrier(clinic.get(), date, calendrier.get());
        // Check if not already take.
        if(isAvailable.isPresent()){
            return new ResponseEntity<>(new ApiResponse(false,"Time shift already taken, please choose another one."),
                    HttpStatus.BAD_REQUEST);
        }
        var patientNom = newDisponibilite.getNom();
        var patientPrenom = newDisponibilite.getPrenom();

        // Finally perform the save operation
        Patients patient = new Patients(patientNom, patientPrenom);
        Patients savedPatient = this.patientRepository.save(patient);
        RendezVous rdv = new RendezVous(clinic.get(), savedPatient,calendrier.get(), date, false);
        this.rendezVousRepository.save(rdv);

        return rdv;
    }



}
