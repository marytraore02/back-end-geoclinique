package geoclinique.geoclinique;

import geoclinique.geoclinique.model.ERole;
import geoclinique.geoclinique.model.Role;
import geoclinique.geoclinique.repository.RoleRepository;
import geoclinique.geoclinique.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import geoclinique.geoclinique.model.Utilisateur;
import geoclinique.geoclinique.model.Utilisateur;


import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class GeoCliniqueApplication implements CommandLineRunner {
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(GeoCliniqueApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//VERIFICATION DE L'EXISTANCE DU ROLE ADMIN AVANT SA CREATION
		if (roleRepository.findAll().size() == 0){
			roleRepository.save(new Role(ERole.ROLE_ADMIN));
			roleRepository.save(new Role(ERole.ROLE_PATIENT));
			roleRepository.save(new Role(ERole.ROLE_CLINIC));
		}
		if (userRepository.findAll().size() == 0){
			Set<Role> roles = new HashSet<>();
			Role role = roleRepository.findByName(ERole.ROLE_ADMIN).get();
			roles.add(role);
			Utilisateur admin = new Utilisateur(
//					"Mary",
//					"TRAORE",
//					"+223 93 77 15 53",
//					"https://png.pngtree.com/png-clipart/20190924/original/pngtree-user-vector-avatar-png-image_4830521.jpg",

					"mary",
					"mary@gmail.com",
					encoder.encode( "mary123"));
			admin.setRoles(roles);
			userRepository.save(admin);

		}
	}


}
