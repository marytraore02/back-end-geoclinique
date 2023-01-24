package geoclinique.geoclinique;

import geoclinique.geoclinique.configuration.ImageConfig;
import geoclinique.geoclinique.model.*;
import geoclinique.geoclinique.repository.*;
import geoclinique.geoclinique.service.ClinicsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import geoclinique.geoclinique.model.Utilisateur;


import java.time.LocalTime;
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
	@Autowired
	ClinicsRepository clinicsRepository;
	@Autowired
	PatientRepository patientRepository;
	@Autowired
	CalendrierRepository calendrierRepository;

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

		// CREATION DE UTILISATEUR
		if (userRepository.findAll().size() == 0){
			Set<Role> roles = new HashSet<>();
			Role role = roleRepository.findByName(ERole.ROLE_ADMIN).get();
			roles.add(role);
			Utilisateur user = new Utilisateur(
					"mary",
					"marytra292@gmail.com",
					encoder.encode( "mary123")
			);
			user.setRoles(roles);
			userRepository.save(user);
		}

		// CREATION DE L'ADMINISTRATEUR
		if (userRepository.findAll().size() == 1){
			Set<Role> roles = new HashSet<>();
			Role role = roleRepository.findByName(ERole.ROLE_ADMIN).get();
			roles.add(role);
			Admin admin = new Admin(
					"marytraore",
					"marytra292@gmail.com",
					encoder.encode( "mary123"),
					"TRAORE",
					"Mary",
					"+223 93 77 15 53"
					);
			admin.setRoles(roles);
			userRepository.save(admin);
		}

			// CREATION D'UNE CLINIC
		if (clinicsRepository.findAll().size() == 0) {
			Set<Role> roles = new HashSet<>();
			Role role = roleRepository.findByName(ERole.ROLE_CLINIC).get();
			roles.add(role);
			Clinics clinics = new Clinics(
					"clinic",
					"clinic@gmail.com",
					encoder.encode("clinic123"),
					"Pastere",
					"Une court description de la clinic pastere",
					"+223 93 77 15 53",
					"Bamako",
					"Bamako Hamdalaye ACI 2000",
					"longitude",
					"latitude"
			);
			clinics.setRoles(roles);
			this.clinicsRepository.save(clinics);
		}

		// CREATION D'UN PATIENT
		if (patientRepository.findAll().size() == 0) {
			Set<Role> roles = new HashSet<>();
			Role role = roleRepository.findByName(ERole.ROLE_PATIENT).get();
			roles.add(role);
			Patients patient = new Patients(
					"patient",
					"patient@gmail.com",
					encoder.encode("patient123"),
					"Patient",
					"PATIENT",
					"Homme",
					"1998/02/24",
					"+223 93 77 15 53"
			);
			patient.setRoles(roles);
			this.patientRepository.save(patient);
		}

		// HORAIRE DU CALENDRIER
		Set<Calendrier> calendrierSet = new HashSet<>();
		Calendrier calendrier1 = new Calendrier(1L, LocalTime.of(8, 0), LocalTime.of(9, 0));
		Calendrier calendrier2 = new Calendrier(2L, LocalTime.of(9, 0), LocalTime.of(10, 0));
		Calendrier calendrier3 = new Calendrier(3L, LocalTime.of(10, 0), LocalTime.of(11, 0));
		Calendrier calendrier4 = new Calendrier(4L, LocalTime.of(11, 0), LocalTime.of(12, 0));
		Calendrier calendrier5 = new Calendrier(5L, LocalTime.of(13, 0), LocalTime.of(14, 0));
		Calendrier calendrier6 = new Calendrier(6L, LocalTime.of(14, 0), LocalTime.of(15, 0));
		Calendrier calendrier7 = new Calendrier(7L, LocalTime.of(15, 0), LocalTime.of(16, 0));
		Calendrier calendrier8 = new Calendrier(8L, LocalTime.of(16, 0), LocalTime.of(17, 0));
		calendrierSet.add(calendrier1);
		calendrierSet.add(calendrier2);
		calendrierSet.add(calendrier3);
		calendrierSet.add(calendrier4);
		calendrierSet.add(calendrier5);
		calendrierSet.add(calendrier6);
		calendrierSet.add(calendrier7);
		calendrierSet.add(calendrier8);
		this.calendrierRepository.saveAll(calendrierSet);



	}



}
