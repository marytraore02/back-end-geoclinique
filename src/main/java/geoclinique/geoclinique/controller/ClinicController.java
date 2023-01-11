package geoclinique.geoclinique.controller;

import geoclinique.geoclinique.configuration.EmailConstructor;
import geoclinique.geoclinique.configuration.ImageConfig;

import geoclinique.geoclinique.model.Clinics;
import geoclinique.geoclinique.model.ERole;
import geoclinique.geoclinique.model.Role;
import geoclinique.geoclinique.model.Utilisateur;
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
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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


    @ApiOperation(value = "Creation compte clinic")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody ClinicRequest clinicsRequest,
                                          @RequestParam(value = "file", required = false) MultipartFile file)
            throws IOException {

        if (clinicsRepository.existsBynomClinic(clinicsRequest.getNomClinic())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Le nom de la clinic exist"));
        }
        if (userRepository.existsByEmail(clinicsRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("L'email exist"));
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
        mailSender.send(emailConstructor.constructNewUserEmail(clinics));

//
        return ResponseEntity.ok(new MessageResponse("Compte clinic creer avec succes!"));
    }



//    @PreAuthorize("hasRole('USER') or hasRole('CLINIC') or hasRole('ADMIN')")
    @GetMapping("/read")
    public List<Clinics> Afficher(){
        return clinicsServices.read();
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
