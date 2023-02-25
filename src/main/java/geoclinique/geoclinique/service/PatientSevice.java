package geoclinique.geoclinique.service;

import geoclinique.geoclinique.Api.DtoMapper.RendezVousMapper;
import geoclinique.geoclinique.Api.DtoViewModel.Request.RdvMedecinRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Request.NewRdvRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Response.ApiResponse;
import geoclinique.geoclinique.Api.DtoViewModel.Response.DisponibiliteMedecinResponse;
import geoclinique.geoclinique.dto.Message;
import geoclinique.geoclinique.model.Motif;
import geoclinique.geoclinique.model.Patients;
import geoclinique.geoclinique.model.RendezVous;
import geoclinique.geoclinique.model.Utilisateur;
import geoclinique.geoclinique.payload.response.JwtResponse;
import geoclinique.geoclinique.payload.response.MessageResponse;
import geoclinique.geoclinique.repository.*;
import geoclinique.geoclinique.security.services.UserDetailsImpl;
import geoclinique.geoclinique.util.TweakResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
    MotifRepository motifRepository;
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
    public List<Motif> readMotif(){
        return motifRepository.findAll();
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
//    public boolean existsByConatct(String contact){
//        return patientRepository.existsByContactPatient(contact);
//    }
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
//        Patients patient = patientRepository.findById(id).orElse(null);
//        return patientRepository.save(patient);
        return patientRepository.findById(id)
                .map(p->{
                    p.setNomEtPrenom((patients.getNomEtPrenom()));
                    p.setContact(patients.getContact());
                    p.setDate(patients.getDate());
                    p.setUsername(patients.getUsername());
                    p.setEmail(patients.getEmail());
//                    p.setPassword(encoder.encode(patients.getPassword()));
                    p.setSexePatient(patients.getSexePatient());
                    return patientRepository.save(p);
                }).orElseThrow(()-> new RuntimeException("Patient non trouvé !"));
        // TODO Auto-generated method stub

        //return patientRepository.save(patients);

    }
    public String delete(Long id){
        patientRepository.deleteById(id);
        return "Supprimer avec succes";
    }

    // List de disponibilite d'un medecin par jour
    public List<DisponibiliteMedecinResponse> listAllRdvMedecin(RdvMedecinRequest medecinRdv){
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
            var shift = this.calendrierRepository.findById(j).get();
            System.out.print(shift);

            // L'OBJET dummy PREND LES DISPONIBILITES VALIDE
            DisponibiliteMedecinResponse dummy = new DisponibiliteMedecinResponse(shift.getId(), shift.getHeureDebut() +" - "+shift.getHeureFin(), true);
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
    public ResponseEntity<?> save(Utilisateur currentUser, NewRdvRequest newRdv) throws ParseException {

        var medecin = this.medecinsRepository.findById(newRdv.getMedecinId());
        var calendrier = this.calendrierRepository.findById(newRdv.getCalendrierId());
        var motif = this.motifRepository.findById(newRdv.getMotifId());
        var date = LocalDate.parse(newRdv.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            System.out.println("Date request: " +
                    date);
        String sDate1="1998/12/31";
        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(newRdv.getDate());
        System.out.println(sDate1+"t"+date1);

            if(date1.before(new Date())){
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse("Vous ne pouvez pas prendre une date ultérieur pour votre rendez-vous"));
                /*return ResponseEntity.badRequest().body(new ApiResponse(false,"Vous ne pouvez pas prendre une date ultérieur pour votre rendez-vous")
                        );*/
            }

        if(medecin.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Medecin non trouvé."),
                    HttpStatus.NOT_FOUND);
        }
        if(calendrier.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"calendar does not exist, please try again."),
                    HttpStatus.BAD_REQUEST);
        }
        if(motif.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Motif non trouvé."),
                    HttpStatus.BAD_REQUEST);
        }

        var isAvailable = this.rendezVousRepository.findByMedecinsAndDateAndCalendrier(medecin.get(), date, calendrier.get());
        // Check if not already take.
        if(isAvailable.isPresent()){
//            return new ResponseEntity(new Message("Ce email existe déjà"), HttpStatus.BAD_REQUEST);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Heure occupé, Veuillez choisir un autre heure."));
//            return new ResponseEntity(new Message("Heure occupé, Veuillez choisir un autre heure."),
//                    HttpStatus.BAD_REQUEST);
        }

        var curentPatient = this.patientRepository.findById(currentUser.getId());
        System.err.println(currentUser);

        if(curentPatient.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Patient not found."),
                    HttpStatus.NOT_FOUND);
        }
        RendezVous rdv = new RendezVous(motif.get(), medecin.get(), curentPatient.get(), calendrier.get(), date, false);
        this.rendezVousRepository.save(rdv);

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Ok"));


    }


}
