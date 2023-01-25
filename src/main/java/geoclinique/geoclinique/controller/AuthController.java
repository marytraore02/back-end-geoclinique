package geoclinique.geoclinique.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import geoclinique.geoclinique.model.Admin;
import geoclinique.geoclinique.model.ERole;
import geoclinique.geoclinique.model.Role;
import geoclinique.geoclinique.model.Utilisateur;
import geoclinique.geoclinique.payload.request.ClinicRequest;
import geoclinique.geoclinique.payload.request.LoginRequest;
import geoclinique.geoclinique.payload.request.PatientRequest;
import geoclinique.geoclinique.payload.request.SignupRequest;
import geoclinique.geoclinique.payload.response.JwtResponse;
import geoclinique.geoclinique.payload.response.MessageResponse;
import geoclinique.geoclinique.repository.*;
import geoclinique.geoclinique.security.jwt.JwtUtils;
import geoclinique.geoclinique.security.services.CrudService;
import geoclinique.geoclinique.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins ={ "http://localhost:4200/", "http://localhost:8100/", "http://localhost:8200/"  }, maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private CrudService crudService;
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;
  @Autowired
  PatientRepository patientRepository;
  @Autowired
  ClinicsRepository clinicsRepository;
  @Autowired
  AdminRepository adminRepository;
  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;
//  @Autowired
//  private OAuth2AuthorizedClientService loadAuthorizedClientService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    //Connexion en fonction du type
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    //Stockage des infos du user par Spring boot
    SecurityContextHolder.getContext().setAuthentication(authentication);

    //On creer le token et stocke ds le cookies
    String jwt = jwtUtils.generateJwtToken(authentication);

    //creation d'instance du user en fonction de UserDetailImpl
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }
    //Verification si le username exist deja ds la table clinic
    ClinicRequest clinicRequest = new ClinicRequest();
    if (clinicsRepository.existsByUsername(clinicRequest.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Username non valide"));
    }
    //Verification si le username exist ds la table patient
    PatientRequest patientRequest = new PatientRequest();
    if (patientRepository.existsByUsername(patientRequest.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Username non valide"));
    }
    //Verification du username ds la admin
    Admin admin = new Admin();
    if (adminRepository.existsByUsername(admin.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Username non valide"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

     //Create new user's account
    Utilisateur user = new Utilisateur(signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
            break;

          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }



}
