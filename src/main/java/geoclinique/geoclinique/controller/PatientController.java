package geoclinique.geoclinique.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import geoclinique.geoclinique.Api.DtoViewModel.Request.NewRdvRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Request.RdvMedecinRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Response.ApiResponse;
import geoclinique.geoclinique.Api.DtoViewModel.Response.RdvMedecinResponse;
import geoclinique.geoclinique.configuration.EmailConstructor;
import geoclinique.geoclinique.dto.Message;
import geoclinique.geoclinique.model.*;
import geoclinique.geoclinique.payload.request.PatientRequest;
import geoclinique.geoclinique.payload.response.MessageResponse;
import geoclinique.geoclinique.repository.*;
import geoclinique.geoclinique.service.PatientSevice;
import geoclinique.geoclinique.util.TweakResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;

import java.io.IOException;
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
    CliniqueRepository clinicsRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdminRepository adminRepository;

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
    public ResponseEntity<?> registerPatient(@RequestParam(value = "data") String patientsRequest1)
            throws IOException {

        //@RequestParam(value = "file", required = false) MultipartFile file


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
        //Verification si le CONTACT exist ds la BASE
        if (patientRepository.existsByContactPatient(patientRequest.getContactPatient())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Le numero exist, veuillez donner un autre"));
        }



        // Create new user's account
        Patients patients =
                new Patients(patientRequest.getUsername(), patientRequest.getEmail(),
                        encoder.encode(patientRequest.getPassword()),
                        patientRequest.getNomPatient(), patientRequest.getPrenomPatient(),
                        patientRequest.getSexePatient(),  patientRequest.getNaissancePatient(),
                        patientRequest.getContactPatient());

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
//        if (file != null) {
//            patients.setImage(ImageConfig.save("patient", file, patients.getPrenomPatient()));
//        }
        patientRepository.save(patients);
        //mailSender.send(emailConstructor.constructNewUserEmail(patients));
        return ResponseEntity.ok(new MessageResponse("Compte patient creer avec succes!"));
    }

    @GetMapping("/read")
    public ResponseEntity<List<Patients>> Afficher(){
        List<Patients> patients = patientSevice.read();
        return new ResponseEntity(patients, HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @ApiOperation(value = "Mise à jour du comptes patient")
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@RequestParam(value = "data") String acti,
                                         @PathVariable("id") Long id,
                                         @RequestParam(value = "file", required = false) MultipartFile file)
            throws IOException {
        Patients patients = null;

        try {
            patients = new JsonMapper().readValue(acti, Patients.class);
//            Random e = new Random();
//            e.nextInt(8);
//            if (file != null) {
//                patients.setImage(ImageConfig.save("patient", file, patients.getPrenomPatient()));
//            }
            patientSevice.modifier(patients);
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
    @PostMapping("/rdv/save")
    public ResponseEntity<?> save(@Valid @RequestBody NewRdvRequest newRdv){
        try{
            var save = this.patientSevice.save(newRdv);
            return ResponseEntity.ok(save);
        } catch (Exception e){
            return new ResponseEntity(new Message("Erreur de sauvagarde"), HttpStatus.OK);
        }
    }

    // Obtenir la liste des disponibites d'un medecin par jour
    @PostMapping("/rdv")
    public ResponseEntity<? extends Object> listRdvMedecin(@Valid @RequestBody RdvMedecinRequest rdvMedecin){
        var result = this.patientSevice.listAllRdvMedecin(rdvMedecin);

        try{
            if(result==null)
                return new ResponseEntity<>(new ApiResponse(false,"Clinique non trovée.."),
                        HttpStatus.NOT_FOUND);

            return ResponseEntity.ok(result);

      }catch (Exception e){
          return new ResponseEntity(new Message("Erreur"), HttpStatus.OK);
      }


    }


//    @GetMapping("/test")
//    public ResponseEntity<? extends Object> list(@Valid @RequestBody RdvMedecinRequest medecinRdv){
//        var response = this.tweakResponse.listAllRdvByStatus(medecinRdv);
//        return ResponseEntity.ok(response);
//    }

}
