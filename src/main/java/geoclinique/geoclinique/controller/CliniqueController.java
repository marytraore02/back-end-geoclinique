package geoclinique.geoclinique.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import geoclinique.geoclinique.configuration.EmailConstructor;
import geoclinique.geoclinique.configuration.ImageConfig;

import geoclinique.geoclinique.configuration.ImgConf;
import geoclinique.geoclinique.dto.Message;
import geoclinique.geoclinique.model.*;
import geoclinique.geoclinique.payload.request.CliniqueRequest;
import geoclinique.geoclinique.payload.response.MessageResponse;
import geoclinique.geoclinique.repository.*;
import geoclinique.geoclinique.security.jwt.JwtUtils;
import geoclinique.geoclinique.security.services.CrudService;
import geoclinique.geoclinique.service.CliniqueServices;
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

import java.io.IOException;
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
    CliniqueRepository clinicsRepository;
    @Autowired
    PatientRepository patientRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AdminRepository adminRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    CliniqueServices clinicsServices;
    @Autowired
    private EmailConstructor emailConstructor;

    @Autowired
    private JavaMailSender mailSender;



    @ApiOperation(value = "Creation de compte clinique")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestParam(value = "cliniqueRequest1") String cliniqueRequest1,
                                          @Param("agrementClinic") MultipartFile agrementClinic)
            throws IOException {
        //Conversion des donnees data en JSON
        CliniqueRequest cliniqueRequest = new JsonMapper().readValue(cliniqueRequest1, CliniqueRequest.class);

        //Verification si le nom exist ds la table clinics
        if (clinicsRepository.existsByNomClinique(cliniqueRequest.getNomClinique())) {
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
        Clinique clinics =
                new Clinique(cliniqueRequest.getUsername(), cliniqueRequest.getEmail(),
                        encoder.encode(cliniqueRequest.getPassword()),
                        cliniqueRequest.getNomClinique(), cliniqueRequest.getDescriptionClinique(),
                        cliniqueRequest.getAdresseClinique(), cliniqueRequest.getVilleClinique(),
                        cliniqueRequest.getContactClinique(), cliniqueRequest.getLongitudeClinique(),
                        cliniqueRequest.getLatitudeClinique());


        //Enregistrement de la photo
        String img = StringUtils.cleanPath(agrementClinic.getOriginalFilename());
        cliniqueRequest.setAgrementClinique(img);
        String uploaDir = "\\Users\\siby\\Desktop\\geo-clinique\\src\\ressources\\static\\images\\clinics";
        ImgConf.saveimg(uploaDir, img, agrementClinic);


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
        clinics.setRoles(roles);
        clinics.setStatusClinique(false);
//        if (file != null) {
//            clinics.setAgrementClinic(ImageConfig.save("clinic", file, clinics.getNomClinic()));
//        }
        clinicsRepository.save(clinics);
        //mailSender.send(emailConstructor.constructNewUserEmail(clinics));
        return ResponseEntity.ok(new MessageResponse("Compte clinic creer avec succes!"));
    }


    @ApiOperation(value = "Lister les comptes clinique")
    @PreAuthorize("hasRole('PATIENT') or hasRole('CLINIC') or hasRole('ADMIN')")
    @GetMapping("/read")
    public ResponseEntity<List<Clinique>> Afficher(){
        List<Clinique> clinics = clinicsServices.read();
        return new ResponseEntity(clinics, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CLINIC') or hasRole('ADMIN')")
    @ApiOperation(value = "Mise à jour de comptes clinique")
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@RequestParam(value = "data") String acti,
                                         @PathVariable("id") Long id,
                                         @RequestParam(value = "file", required = false) MultipartFile file)
            throws IOException {
        Clinique clinics = null;

        try {
            clinics = new JsonMapper().readValue(acti, Clinique.class);
//            Random e = new Random();
//            e.nextInt(8);
            if (file != null) {
                clinics.setAgrementClinique(ImageConfig.save("clinic", file, clinics.getNomClinique()));
            }
            clinicsServices.modifier(id, clinics);
            return new ResponseEntity(new Message("Clinique modifié avec success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Message("Erreur de modification"), HttpStatus.OK);
        }
    }

    @PreAuthorize("hasRole('CLINIC') or hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('CLINIC') or hasRole('ADMIN')")
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



}
