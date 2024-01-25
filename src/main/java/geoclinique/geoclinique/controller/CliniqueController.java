package geoclinique.geoclinique.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import geoclinique.geoclinique.Api.DtoViewModel.Request.RdvMedecinRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Request.TodayRdvRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Response.TodayRdvResponse;
import geoclinique.geoclinique.configuration.EmailConstructor;
import geoclinique.geoclinique.configuration.ImageConfig;

import geoclinique.geoclinique.configuration.ImgConf;
import geoclinique.geoclinique.dto.Message;
import geoclinique.geoclinique.model.*;
import geoclinique.geoclinique.payload.request.CliniqueRequest;
import geoclinique.geoclinique.payload.response.MessageResponse;
import geoclinique.geoclinique.repository.*;
import geoclinique.geoclinique.security.CurrentUser;
import geoclinique.geoclinique.security.jwt.JwtUtils;
import geoclinique.geoclinique.service.*;
import org.springframework.validation.BindingResult;
import geoclinique.geoclinique.security.services.CrudService;
import geoclinique.geoclinique.security.services.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
//import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import javax.validation.ValidationException;


import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin(origins ={ "http://localhost:4200/", "http://localhost:8100/", "http://localhost:8200/"  }, maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/clinic")
@Api(value = "hello", description = "CRUD CLINIQUE")
public class CliniqueController {
    @Autowired
    private CrudService crudService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UtilisateurService utilisateurService;
    @Autowired
    CliniqueRepository clinicsRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    RendezVousRepository rendezVousRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MedecinsService medecinsService;
    @Autowired
    CliniqueServices cliniqueServices;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    SpecialiteService specialiteService;
    @Autowired
    CliniqueServices clinicsServices;
    @Autowired
    private EmailConstructor emailConstructor;

    @Autowired
    private JavaMailSender mailSender;



    @ApiOperation(value = "Creation de compte clinique")
    @PostMapping("/signup/{idSpec}")
    public ResponseEntity<?> registerUser(@RequestParam(value = "data") String cliniqueRequest1,
                                          @PathVariable("idSpec") Long idSpec,
                                          @RequestParam(value = "file", required = false) MultipartFile file,
                                          @RequestParam(value = "fil", required = false) MultipartFile fil
                                        )
    //@Param("agrementClinic") MultipartFile agrementClinic
            throws IOException {
        //Conversion des donnees data en JSON
        CliniqueRequest cliniqueRequest = new JsonMapper().readValue(cliniqueRequest1, CliniqueRequest.class);

        //Verification si le nom exist ds la table clinics
        if (userRepository.existsByNomEtPrenom(cliniqueRequest.getNomEtPrenom())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Une clinique du même nom exist, Veuillez donnez un autre nom!"));
        }
        //Verification si le USERNAME exist ds la BASE
        if (userRepository.existsByUsername(cliniqueRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Username non valide"));
        }
        //Verification si EMAIL exist deja ds la BASE
        if (userRepository.existsByEmail(cliniqueRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Ce email est dejà utilisé"));
        }

        // Create new user's account
        Clinique clinics = new Clinique(
                        cliniqueRequest.getNomEtPrenom(),
                        cliniqueRequest.getContact(),
                        cliniqueRequest.getDate(),
                        cliniqueRequest.getImage(),
                        cliniqueRequest.getUsername(),
                        cliniqueRequest.getEmail(),
                        encoder.encode(cliniqueRequest.getPassword()),
                        cliniqueRequest.getDescriptionClinique(),
                        cliniqueRequest.getAdresseClinique(),
                        cliniqueRequest.getVilleClinique(),
                        cliniqueRequest.getLongitudeClinique(),
                        cliniqueRequest.getLatitudeClinique(),
                        cliniqueRequest.getListSpecialiteClinique()
        );

        //Creation des roles
        Set<String> strRoles = cliniqueRequest.getRole();

        //recuperer le role a l'entrer
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_CLINIC)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            //verifier si ca existe dana la base de donnee
            strRoles.forEach(role -> {
                switch (role) {
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_CLINIC)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        Specialites specialites = specialiteService.GetOne(idSpec).get();
        clinics.getListeSpecialiteCli().add(specialites);
        clinics.setRoles(roles);
        clinics.setStatusClinique(false);
        if (file != null) {
            clinics.setAgrementClinique(ImageConfig.save("agrementclinique", file, clinics.getNomEtPrenom()));
        }

                if (fil != null) {
                    clinics.setImage(ImageConfig.save("clinique", fil, clinics.getNomEtPrenom()));
                }

        Clinique cli = clinicsRepository.save(clinics);
                Notification notif = new Notification();
                notif.setClinique(cli);
                notif.setDatenotif(new Date());
                notif.setTitre("Un nouveau compte clinique vient d'être créer");
                notificationService.creer(notif);
        mailSender.send(emailConstructor.constructNewUserEmail(clinics));
        return ResponseEntity.ok(new MessageResponse("Compte clinic creer avec succes!"));
    }


    @ApiOperation(value = "Lister les comptes clinique")
    //@PreAuthorize("hasRole('PATIENT') or hasRole('CLINIC') or hasRole('ADMIN')")
    @GetMapping("/read")
    public ResponseEntity<List<Clinique>> Afficher(){
        List<Clinique> clinics = clinicsServices.read();
        return new ResponseEntity(clinics, HttpStatus.OK);
    }

    @ApiOperation(value = "List des clinique valider")
    @GetMapping("/cliniquevalide")
    public Iterable<List<Clinique>> getRdvValide(){
        return clinicsRepository.getListCliniqueValide();
    }

    @PreAuthorize("hasRole('CLINIC') or hasRole('ADMIN')")
    @ApiOperation(value = "Mise à jour de comptes clinique")
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@RequestParam(value = "data") String acti,
                                         @PathVariable("id") Long id,
                                         @RequestParam(value = "file", required = false) MultipartFile file,
                                         @RequestParam(value = "fil", required = false) MultipartFile fil
    )
            throws IOException {
        Clinique clinics = null;

        try {
            clinics = new JsonMapper().readValue(acti, Clinique.class);
//            Random e = new Random();
//            e.nextInt(8);
            if (fil != null) {
                clinics.setImage(ImageConfig.save("clinique", fil, clinics.getNomEtPrenom()));
            }
            if (file != null) {
                clinics.setAgrementClinique(ImageConfig.save("agrementclinique", file, clinics.getNomEtPrenom()));
            }
            clinicsServices.modifier(id, clinics);
            return new ResponseEntity(new Message("Clinique modifié avec success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Message("Erreur de modification"), HttpStatus.OK);
        }
    }

    //@PreAuthorize("hasRole('CLINIC') or hasRole('ADMIN')")
    @ApiOperation(value = "Suppression de comptes clinique")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> supprimer(@PathVariable long id) {
        try {
            if(!clinicsServices.existsById(id))
                return new ResponseEntity(new Message("Id n'existe pas"), HttpStatus.NOT_FOUND);
            clinicsServices.delete(id);
            return new ResponseEntity(new Message("Clinique supprimer avec succès"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Message("Erreur de suppression"), HttpStatus.OK);
        }
    }

    //@PreAuthorize("hasRole('CLINIC') or hasRole('ADMIN')")
    @ApiOperation(value = "Afficher une clinique")
    @GetMapping("/get/{id}")
    public ResponseEntity<Clinique> getById(@PathVariable("id") Long id){
        if(!clinicsServices.existsById(id))
            return new ResponseEntity(new Message("Id n'existe pas"), HttpStatus.NOT_FOUND);
        Clinique clinics = clinicsServices.GetOne(id).get();
        return new ResponseEntity(clinics, HttpStatus.OK);
    }

    @GetMapping("/resetPassword/{email}")
    public ResponseEntity<String> resetPassword(@PathVariable("email") String email) {
        Utilisateur user = userRepository.findByEmail(email);
        if (user == null) {
            return new ResponseEntity<String>("Email non fourni", HttpStatus.BAD_REQUEST);
        }
        clinicsServices.resetPassword(user);
        return new ResponseEntity<String>("Email envoyé!", HttpStatus.OK);
    }


    // LA METHODE POUR VOIR LA LISTE DES RENDEZ-VOUS D'UN MEDECIN
//    @PreAuthorize("hasRole('CLINIC')")
    @PostMapping("rdv/list/{id}")
    @ApiOperation(value = "Afficher les rendez-vous d'un medecin par date")
    public ResponseEntity<? extends Object> today(@PathVariable("id") Long id, @Valid @RequestBody TodayRdvRequest rdvMedecinRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidationException("Erreur lors de l'affichage des rendez-vous du medecin");
        }
        if(!medecinsService.existsById(id))
            return new ResponseEntity(new Message("Id n'existe pas"), HttpStatus.NOT_FOUND);
        Long medecins = rdvMedecinRequest.getMedecinId();
       // var date = LocalDate.parse(rdvMedecinRequest.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        var response = this.cliniqueServices.AllRdvMedecin(medecins, rdvMedecinRequest);
//        if(response==null)
//            return ResponseEntity.ok(new TodayRdvResponse(date, 0, null));
        return ResponseEntity.ok(response);

    }


    // SUPPRESSION D'UN RENDEZ-VOUS
//    @PreAuthorize("hasRole('CLINIC')")
    @DeleteMapping("rdv/{rdvId}")
    @ApiOperation(value = "Supprimer le rendez-vous d'un medecin par date")
    public ResponseEntity<?> delete(@PathVariable String rdvId){
        var response =this.cliniqueServices.deleteEvent(rdvId);
        return ResponseEntity.ok(response);
    }

    // Change the status of an event
//    @PreAuthorize("hasRole('CLINIC')")
    @ApiOperation(value = "Changer le status du rendez-vous d'un medecin par date")
    @PostMapping("rdv/status/{rdvId}")
    public ResponseEntity<?> status(@PathVariable String rdvId){
        RendezVous rdv = rendezVousRepository.getReferenceById(Long.parseLong(rdvId));
        var response = this.cliniqueServices.changeEventStatus(rdvId);

//        Notification notification = new Notification();
//        notification.setClinique((Clinique) response);
//        notification.setDatenotif(new Date());
//        notification.setTitre("Rendez-vous accepter");
//        notification.setDescription("Votre rendez-vous a été accepter avec success");
//        notificationService.creer(notification);

        //return new ResponseEntity(response, HttpStatus.OK);
        return ResponseEntity.ok(response);

    }

    @ApiOperation(value = "Changer le status de la clinique")
    @PostMapping("status/{id}")
    public ResponseEntity<?> ChangeStatus(@PathVariable String id){
        var response = this.cliniqueServices.changeStatusClinique(id);
        return ResponseEntity.ok(response);
    }

//    @ApiOperation(value = "Changer le status du rendez-vous d'un medecin par date")
//    @PostMapping("change/status/{rdvId}")
//    public ResponseEntity<?> Changestatus(@PathVariable("rdvId") Long rdvId){
//        Clinique response = this.clinicsRepository.findById(rdvId.);
//        return ResponseEntity.ok(response);
//    }


}
