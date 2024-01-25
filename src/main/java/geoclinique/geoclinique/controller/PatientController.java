package geoclinique.geoclinique.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import geoclinique.geoclinique.Api.DtoViewModel.Request.NewRdvRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Request.RdvMedecinRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Response.ApiResponse;
import geoclinique.geoclinique.configuration.EmailConstructor;
import geoclinique.geoclinique.configuration.ImageConfig;
import geoclinique.geoclinique.dto.Message;
import geoclinique.geoclinique.model.*;
import geoclinique.geoclinique.payload.request.CliniqueRequest;
import geoclinique.geoclinique.payload.request.PatientRequest;
import geoclinique.geoclinique.payload.response.MessageResponse;
import geoclinique.geoclinique.repository.*;
import geoclinique.geoclinique.security.CurrentUser;
import geoclinique.geoclinique.security.services.UserDetailsImpl;
import geoclinique.geoclinique.service.MedecinsService;
import geoclinique.geoclinique.service.PatientSevice;
import geoclinique.geoclinique.util.TweakResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import javax.validation.ValidationException;


import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins ={ "http://localhost:4200/", "http://localhost:8100/", "http://localhost:8200/"  }, maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/patient")
@Api(value = "hello", description = "CRUD PATIENT")
public class PatientController {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PatientRepository patientRepository;
    @Autowired
    MedecinsService medecinsService;
    @Autowired
    CliniqueRepository clinicsRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MotifRepository motifRepository;
    @Autowired
    PatientSevice patientSevice;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private EmailConstructor emailConstructor;

    @Autowired
    private JavaMailSender mailSender;
    private TweakResponse tweakResponse;


    @ApiOperation(value = "Creation de compte patient")
    @PostMapping("/signup")
    public ResponseEntity<?> registerPatient(@RequestParam(value = "data") String patientsRequest1,
                                             @RequestParam(value = "file", required = false) MultipartFile file)
            throws IOException {


        //Conversion des donnees data en JSON
        PatientRequest patientRequest = new JsonMapper().readValue(patientsRequest1, PatientRequest.class);

        //Verification si le USERNAME exist ds la BASE
        if (userRepository.existsByUsername(patientRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Username non valide"));
        }
        //Verification si EMAIL exist deja ds la BASE
        if (userRepository.existsByEmail(patientRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Ce email est dejà utilisé par un patient"));
        }

        // Create new user's account
        Patients patients = new Patients(
                        patientRequest.getNomEtPrenom(),
                        patientRequest.getContact(),
                        patientRequest.getDate(),
                        patientRequest.getImage(),
                        patientRequest.getUsername(),
                        patientRequest.getEmail(),
                        encoder.encode(patientRequest.getPassword()),
                        patientRequest.getSexePatient()
                );

        //Creation des roles
        Set<String> strRoles =  patientRequest.getRole();

        //recuperer le role a l'entrer
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            //verifier si ca existe dana la base de donnee
            strRoles.forEach(role -> {
                switch (role) {
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        patients.setRoles(roles);
        if (file != null) {
            patients.setImage(ImageConfig.save("patient", file, patients.getUsername()));
        }
        patientRepository.save(patients);
        //mailSender.send(emailConstructor.constructNewUserEmail(patients));
        return ResponseEntity.ok(new MessageResponse("Compte patient creer avec succes!"));
    }

    @GetMapping("/read")
    public ResponseEntity<List<Patients>> Afficher(){
        List<Patients> patients = patientSevice.read();
        return new ResponseEntity(patients, HttpStatus.OK);
    }
    @ApiOperation(value = "Afficher la liste des medecin")
    @GetMapping("/readmedecin")
    public ResponseEntity<List<Medecins>> Read(){
        List<Medecins> medecins = medecinsService.read();
        return new ResponseEntity(medecins, HttpStatus.OK);
    }

    @ApiOperation(value = "Afficher la liste des motifs")
    @GetMapping("/readmotif")
    public ResponseEntity<List<Motif>> ReadMotif(){
        List<Motif> motif = patientSevice.readMotif();
        return new ResponseEntity(motif, HttpStatus.OK);
    }

    @ApiOperation(value = "Afficher un motif")
    @GetMapping("/getMotif/{id}")
    public ResponseEntity<Motif> getByIdMotif(@PathVariable("id") Long id){
        if(!patientSevice.existsByIdMotif(id))
            return new ResponseEntity(new Message("Id n'existe pas"), HttpStatus.NOT_FOUND);
        Motif motif = patientSevice.GetOneMotif(id).get();
        return new ResponseEntity(motif, HttpStatus.OK);
    }


    //@PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @ApiOperation(value = "Mise à jour du comptes patient")
    @PostMapping("/update/{id}")
    public ResponseEntity<Object> update(@RequestParam(value = "data") String acti,
                                         @PathVariable("id") Long id)
            throws IOException {
        //@RequestParam(value = "file", required = false) MultipartFile file
        Patients patients = null;

        try {
            patients = new JsonMapper().readValue(acti, Patients.class);
            if(!patientSevice.existsById(id))
                return new ResponseEntity(new Message("Id n'existe pas"), HttpStatus.NOT_FOUND);
//            Random e = new Random();
//            e.nextInt(8);
//            if (file != null) {
//                patients.setImage(ImageConfig.save("patient", file, patients.getPrenomPatient()));
//            }
            patientSevice.modifier(id,patients);
            return new ResponseEntity(new Message("Patient modifié avec success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Message("Erreur de modification"), HttpStatus.OK);
        }
    }

    //@PreAuthorize("hasRole('CLINIC') or hasRole('ADMIN')")
    @ApiOperation(value = "Suppression de comptes patient")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> supprimer(@PathVariable long id) {
        try {
            if(!patientSevice.existsById(id))
                return new ResponseEntity(new Message("Id n'existe pas"), HttpStatus.NOT_FOUND);
            patientSevice.delete(id);
            return new ResponseEntity(new Message("Patients supprimer avec succès"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Message("Erreur de suppression"), HttpStatus.OK);
        }
    }

    //@PreAuthorize("hasRole('CLINIC') or hasRole('ADMIN')")
    @ApiOperation(value = "Afficher un patient")
    @GetMapping("/get/{id}")
    public ResponseEntity<Clinique> getById(@PathVariable("id") Long id){
        if(!patientSevice.existsById(id))
            return new ResponseEntity(new Message("Id n'existe pas"), HttpStatus.NOT_FOUND);
        Patients patients = patientSevice.GetOne(id).get();
        return new ResponseEntity(patients, HttpStatus.OK);
    }


    //  Ajouter RDV
    @ApiOperation(value = "Ajouter un rendez-vous")
    @PostMapping("/rdv/save/{id}")
//    public ResponseEntity<?> save(@CurrentUser UserDetailsImpl currentUser, @Valid @RequestBody NewRdvRequest newRdv) {
//        System.err.println(currentUser.getEmail());
//        try{
//            var save = this.patientSevice.save(currentUser, newRdv);
//            return ResponseEntity.ok(save);
//        } catch (Exception e){
//            return new ResponseEntity(new Message("Erreur de sauvagarde"), HttpStatus.OK);
//        }
//    }
    public ResponseEntity<?> save(@PathVariable Long id,
                                  @Valid @RequestBody NewRdvRequest newRdv) throws ParseException {
                if(!userRepository.existsById(id))
                    return new ResponseEntity(new Message("Id n'existe pas"), HttpStatus.NOT_FOUND);
        Utilisateur currentUser = userRepository.getReferenceById(id);
            return this.patientSevice.save(currentUser, newRdv);
    }

    // Obtenir la liste des disponibites d'un medecin par jour
    @PostMapping("/rdv")
    public ResponseEntity<? extends Object> listRdvMedecin(@Valid @RequestBody RdvMedecinRequest rdvMedecin, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new ValidationException("Availabilities has errors; Can not fetch all medecin availabilities;");
        }

        var result = this.patientSevice.listAllRdvMedecin(rdvMedecin);

//        try{
            if(result==null)
                return new ResponseEntity<>(new ApiResponse(false,"Clinique non trovée.."),
                        HttpStatus.NOT_FOUND);

            return ResponseEntity.ok(result);
//
//      }catch (Exception e){
//          return new ResponseEntity(new Message("Erreur"), HttpStatus.OK);
//      }


    }


//    @GetMapping("/test")
//    public ResponseEntity<? extends Object> list(@Valid @RequestBody RdvMedecinRequest medecinRdv){
//        var response = this.tweakResponse.listAllRdvByStatus(medecinRdv);
//        return ResponseEntity.ok(response);
//    }

}
