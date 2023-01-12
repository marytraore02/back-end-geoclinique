package geoclinique.geoclinique.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import geoclinique.geoclinique.configuration.EmailConstructor;
import geoclinique.geoclinique.configuration.ImageConfig;

import geoclinique.geoclinique.dto.Message;
import geoclinique.geoclinique.model.*;
import geoclinique.geoclinique.payload.request.ClinicRequest;
import geoclinique.geoclinique.payload.request.LoginRequest;
import geoclinique.geoclinique.payload.response.JwtResponse;
import geoclinique.geoclinique.payload.response.MessageResponse;
import geoclinique.geoclinique.repository.ClinicsRepository;
import geoclinique.geoclinique.repository.RoleRepository;
import geoclinique.geoclinique.repository.UserRepository;
import geoclinique.geoclinique.security.jwt.JwtUtils;
import geoclinique.geoclinique.security.services.CrudService;
import geoclinique.geoclinique.security.services.UserDetailsImpl;
import geoclinique.geoclinique.service.ClinicsServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/clinic")
@Api(value = "hello", description = "CRUD CLINIC")
public class ClinicController {
    @Autowired
    private CrudService crudService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ClinicsRepository clinicsRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ClinicsServices clinicsServices;
    @Autowired
    private EmailConstructor emailConstructor;

    @Autowired
    private JavaMailSender mailSender;


    @ApiOperation(value = "Creation de compte clinic")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestParam(value = "data") String clinicsRequest1,
                                          @RequestParam(value = "file", required = false) MultipartFile file)
            throws IOException {
        //Conversion des donnees data en JSON
        ClinicRequest clinicsRequest = new JsonMapper().readValue(clinicsRequest1, ClinicRequest.class);

        //Verification si le nom exist ds la BDD
        if (clinicsRepository.existsByNomClinic(clinicsRequest.getNomClinic())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Une clinic du même nom exist, Veuillez donnez un autre nom!"));
        }

        //Verification si l'email exist deja ds la BDD
        if (userRepository.existsByEmail(clinicsRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Ce email est dejà utilisé par une clinic"));
        }

        // Create new user's account
        Clinics clinics =
                new Clinics(clinicsRequest.getUsername(), clinicsRequest.getEmail(),
                        encoder.encode(clinicsRequest.getPassword()),
                        clinicsRequest.getNomClinic(), clinicsRequest.getDescriptionClinic(),
                        clinicsRequest.getAdresseClinic(), clinicsRequest.getVilleClinic(),
                        clinicsRequest.getContactClinic(), clinicsRequest.getLongitudeClinic(),
                        clinicsRequest.getLatitudeClinic());

        //Creation des roles
        Set<String> strRoles = clinicsRequest.getRole();

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
        clinics.setStatusClinic(false);
        if (file != null) {
            clinics.setAgrementClinic(ImageConfig.save("clinic", file, clinics.getNomClinic()));
        }
        clinicsRepository.save(clinics);
        //mailSender.send(emailConstructor.constructNewUserEmail(clinics));
        return ResponseEntity.ok(new MessageResponse("Compte clinic creer avec succes!"));
    }


//    @PreAuthorize("hasRole('ADMIN')")
//    @ApiOperation(value = "Creation d'une clinics")
//    @PostMapping("/create/new")
//    public ResponseEntity<?> create(@RequestParam(value = "data") String acti,
//                                    @RequestParam(value = "file", required = false) MultipartFile file)
//            throws IOException {
//        Clinics clinics = null;
//
//            clinics = new JsonMapper().readValue(acti, Clinics.class);
//
//            if(StringUtils.isBlank(clinics.getNomClinic()))
//                return new ResponseEntity(new Message("Le nom de la clinic est obligatoire"), HttpStatus.BAD_REQUEST);
//
//            if(clinicsServices.existsByName(clinics.getNomClinic()))
//                return new ResponseEntity(new Message("Une clinic du même nom exist, Veuillez donnez un autre nom!"), HttpStatus.BAD_REQUEST);
//
//            if(clinicsServices.existsByUsername(clinics.getUsername()))
//                return new ResponseEntity(new Message("Username non valide"), HttpStatus.BAD_REQUEST);
//        //Verification si l'email exist deja ds la BDD
//            if (userRepository.existsByEmail(clinics.getEmail())) {
//                return ResponseEntity
//                        .badRequest()
//                        .body(new MessageResponse("Ce email est dejà utilisé par une clinic"));
//            }
//            if (file != null) {
//                try {
//                    clinics.setAgrementClinic(ImageConfig.save("clinic", file, clinics.getNomClinic()));
//                    clinics.setStatusClinic(true);
//                    clinicsServices.creer(clinics);
//                    return new ResponseEntity(new Message("Clinics creer avec créé avec success"), HttpStatus.OK);
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    return new ResponseEntity(new Message("Erreur de creation"), HttpStatus.OK);
//                }
//            } else {
//                return new ResponseEntity(new Message("Photo introuvable"), HttpStatus.OK);
//            }
//    }

    @ApiOperation(value = "Lister les comptes clinic")
    @PreAuthorize("hasRole('PATIENT') or hasRole('CLINIC') or hasRole('ADMIN')")
    @GetMapping("/read")
    public List<Clinics> Afficher(){
        return clinicsServices.read();
    }

    @ApiOperation(value = "Mise à jour de comptes clinic")
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@RequestParam(value = "data") String acti,
                                         @PathVariable("id") Long id,
                                         @RequestParam(value = "file", required = false) MultipartFile file)
            throws IOException {
        Clinics clinics = null;

        try {
            clinics = new JsonMapper().readValue(acti, Clinics.class);
//            Random e = new Random();
//            e.nextInt(8);
            if (file != null) {
                clinics.setAgrementClinic(ImageConfig.save("clinic", file, clinics.getNomClinic()));
            }
            clinicsServices.modifier(id, clinics);
            return new ResponseEntity(new Message("Clinic modifié avec success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Message("Erreur de modification"), HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Suppression de comptes clinic")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> supprimer(@PathVariable long id) {
        try {
            if(!clinicsServices.existsById(id))
                return new ResponseEntity(new Message("Id n'existe pas"), HttpStatus.NOT_FOUND);
            clinicsServices.delete(id);
            return new ResponseEntity(new Message("Clinics supprimer avec succès"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Message("Erreur de creation"), HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Affichager une clinic")
    @GetMapping("/get/{id}")
    public ResponseEntity<Clinics> getById(@PathVariable("id") Long id){
        if(!clinicsServices.existsById(id))
            return new ResponseEntity(new Message("Id n'existe pas"), HttpStatus.NOT_FOUND);
        Clinics clinics = clinicsServices.GetOne(id).get();
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
