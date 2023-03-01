package geoclinique.geoclinique.controller;

import geoclinique.geoclinique.repository.RoleRepository;
import geoclinique.geoclinique.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/test")
public class TestController {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;
  @GetMapping("/all")
  public String allAccess(Principal all) {
    return "Public content";

//    String user = "NOM D'UTILISATEUR: " + userRepository.findByUsername(all.getName()).get().getUsername() + "  EMAIL:  "+
//            userRepository.findByUsername(all.getName()).get().getEmail();
//    return "Bienvenue, " + user;
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('CLINIC') or hasRole('ADMIN')")
  public String userAccess(Principal user)
  {

    return "User content";
//    return "Bienvenue, " + userRepository.findByUsername(user.getName()).get().getUsername() +
//            roleRepository.findByName(ERole.ROLE_USER).get().getName();
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('CLINIC')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess(Principal admin ) {
    return "Admin content";

//    return "Bienvenue " + " "+ userRepository.findByUsername(admin.getName()).get().getUsername()  + " "+
//            roleRepository.findByName(ERole.ROLE_ADMIN).get().getName()
//            ;
  }
}
