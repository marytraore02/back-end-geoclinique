package geoclinique.geoclinique;

import geoclinique.geoclinique.model.ERole;
import geoclinique.geoclinique.model.Role;
import geoclinique.geoclinique.repository.RoleRepository;
import geoclinique.geoclinique.repository.UserRepository;
import geoclinique.geoclinique.service.ClinicsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import geoclinique.geoclinique.model.Utilisateur;
import geoclinique.geoclinique.model.Utilisateur;


import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class GeoCliniqueApplication implements CommandLineRunner{
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(GeoCliniqueApplication.class, args);


//		ApplicationContext ctx = SpringApplication.run(GeoCliniqueApplication.class, args);
//		ClinicsServices utilisateurService = ctx.getBean(ClinicsServices.class);
//
//		Role admin = new Role();
//		admin.setId(1L);
//		admin.setName(ERole.ROLE_ADMIN);
//
//		Role clinic = new Role();
//		clinic.setId(2L);
//		clinic.setName(ERole.ROLE_CLINIC);
//
//		Role patient = new Role();
//		patient.setId(3L);
//		patient.setName(ERole.ROLE_PATIENT);
//
//		utilisateurService.create(admin);
//		utilisateurService.create(clinic);
//		utilisateurService.create(patient);
//
//
//		if (utilisateurService.getByEmail("mary@gmail.com") == null) {
//
//		// creation des super administrateurs
//		Utilisateur mary = new Utilisateur();
////		mary.statusClinic(true);
//		mary.setId(1L);
//		mary.setUsername("mary");
//		mary.setEmail("mary@gmail.com");
//		mary.setPassword("mary123");
//		mary.setActive(true);
//
//		utilisateurService.creer(mary);
//		}


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

					"marytraore",
					"marytra292@gmail.com",
					encoder.encode( "mary123"));
			admin.setRoles(roles);
			userRepository.save(admin);

		}
	}



}
