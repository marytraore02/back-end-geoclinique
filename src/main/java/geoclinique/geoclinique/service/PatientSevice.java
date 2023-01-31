package geoclinique.geoclinique.service;

import geoclinique.geoclinique.Api.DtoMapper.RendezVousMapper;
import geoclinique.geoclinique.Api.DtoViewModel.Request.RdvMedecinRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Request.NewRdvRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Response.ApiResponse;
import geoclinique.geoclinique.Api.DtoViewModel.Response.RdvMedecinResponse;
import geoclinique.geoclinique.model.Patients;
import geoclinique.geoclinique.model.RendezVous;
import geoclinique.geoclinique.repository.*;
import geoclinique.geoclinique.util.TweakResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PatientSevice {
    @Autowired
    CliniqueRepository clinicsRepository;
    @Autowired
    MedecinsRepository medecinsRepository;
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


    // List de disponibilite d'un medecin par jour
    public List<RdvMedecinResponse> listAllRdvMedecin(RdvMedecinRequest medecinRdv){
    //System.err.print(medecinRdv.getMedecinId());
        // RECUPERER L'ID DU MEDECIN
        var medecin = this.medecinsRepository.getReferenceById(medecinRdv.getMedecinId());
        //System.err.println(medecin);

        // RECUPERER LA DATE DISPONIBILITE DU MEDECIN
        var date = LocalDate.parse(medecinRdv.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        //System.out.println(date);

        // VERIFIER SI L'ID DU MEDECIN EXIST
        if(medecin == null){
            return null;
        }

        // LA VARIABLE QUI PREND EN ENTRER L"IL DU MEDECIN ET LA DATE
        var RdvMedecin = this.rendezVousRepository.findAllByMedecinsAndDate(medecin, date);

        // LA VARIABLE QUI VA RETOURNER LA LISTE DES HORAIRES DE DISPONIBILITE DU JOUR
        var MedecinRdvList =
                RdvMedecin.stream().sorted((a1, a2) -> {
                            System.out.printf("sort: %s; %s\n", a1, a2);
                            return a1.getCalendrier().getId().compareTo(a2.getCalendrier().getId());
                        })

                        // RETOURNE LES HORAIRES INDISPONIBLE D'UNE JOURNEE
                        .map(a -> this.rendezVousMapper.toRdvMedecinDto(a))
                        .collect(Collectors.toList());
        // medecinRdvList VA NOUS RETOURNER LA LISTE DES HORAIRES DEJA PRISES
        //return MedecinRdvList;


        // LA VARIABLE QUI VA RETOURNER LE NOMBRE TOTAL DES HORAIRES
        int shiftNb = this.calendrierRepository.findAll().size()+1;
        System.err.println(shiftNb);

        // ON PARCOURS LA LISTE DES HORAIRES
        for(long i = 1 ; i < shiftNb; i++){
            Long j = Long.valueOf(i);
            // shift PREND CHAQUE CHAMP DE LA LISTE DE MANIERE UNIQUE
            var shift = this.calendrierRepository.getOne(j);
            System.out.print(shift);

            // L'OBJET dummy PREND LES DISPONIBILITES VALIDE
            RdvMedecinResponse dummy = new RdvMedecinResponse(shift.getId(), shift.getHeureDebut() +" - "+shift.getHeureFin(), true);
            if (!MedecinRdvList.contains(dummy))
            {
                MedecinRdvList.add(dummy);
            }

        }

        // LA METHODE RETOURNE LA LISTE DES HORAIRES DISPONIBLES D'UNE JOURNEE
        //return this.tweakResponse.listAllRdvByStatus(MedecinRdvList);

        return MedecinRdvList;
    }


     //Ajouter une RDV
    public Object save(NewRdvRequest newRdv){

        var medecin = this.medecinsRepository.findById(newRdv.getMedecinId());
        var calendrier = this.calendrierRepository.findById(newRdv.getCalendrierId());
        var date = LocalDate.parse(newRdv.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if(medecin.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Medecin non trouvé."),
                    HttpStatus.NOT_FOUND);
        }
        if(calendrier.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"calendar does not exist, please try again."),
                    HttpStatus.BAD_REQUEST);
        }

        var isAvailable = this.rendezVousRepository.findByMedecinsAndDateAndCalendrier(medecin.get(), date, calendrier.get());
        // Check if not already take.
        if(isAvailable.isPresent()){
            return new ResponseEntity<>(new ApiResponse(false,"Heure occupé, Veuillez choisir un autre heure."),
                    HttpStatus.BAD_REQUEST);
        }
        var patientNom = newRdv.getNom();
        var patientPrenom = newRdv.getPrenom();

        // Finally perform the save operation
        Patients patient = new Patients(patientNom, patientPrenom);
        Patients savedPatient = this.patientRepository.save(patient);
        RendezVous rdv = new RendezVous(medecin.get(), savedPatient,calendrier.get(), date, false);
        this.rendezVousRepository.save(rdv);

        return rdv;
    }



}
