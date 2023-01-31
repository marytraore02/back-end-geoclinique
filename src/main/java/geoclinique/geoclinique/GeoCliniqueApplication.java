package geoclinique.geoclinique;

import geoclinique.geoclinique.model.*;
import geoclinique.geoclinique.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import geoclinique.geoclinique.model.Utilisateur;


import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

@SpringBootApplication
public class GeoCliniqueApplication implements CommandLineRunner{
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	AdminRepository adminRepository;

	@Autowired
	MedecinsRepository medecinsRepository;
	@Autowired
			RendezVousRepository rendezVousRepository;
	@Autowired
			SpecialiteRepository specialiteRepository;

	@Autowired
	CliniqueRepository clinicsRepository;
	@Autowired
	PatientRepository patientRepository;
	@Autowired
	CalendrierRepository calendrierRepository;

	public static void main(String[] args) {
		SpringApplication.run(GeoCliniqueApplication.class, args);
	}
	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("TimeZone"));
	}
	@Override
	public void run(String... args) throws Exception {

		adminRepository.deleteAll();
		calendrierRepository.deleteAll();
		clinicsRepository.deleteAll();
		medecinsRepository.deleteAll();
		patientRepository.deleteAll();
		rendezVousRepository.deleteAll();
		//roleRepository.deleteAll();
		specialiteRepository.deleteAll();
		userRepository.deleteAll();

		// CREATION DES ROLES
		if (roleRepository.findAll().size() == 0) {
			roleRepository.save(new Role(ERole.ROLE_ADMIN));
			roleRepository.save(new Role(ERole.ROLE_PATIENT));
			roleRepository.save(new Role(ERole.ROLE_CLINIC));
		}

		// CREATION DE UTILISATEUR
		if (userRepository.findAll().size() == 0) {
			Set<Role> roles = new HashSet<>();
			Role role = roleRepository.findByName(ERole.ROLE_ADMIN).get();
			roles.add(role);
			Utilisateur user = new Utilisateur(
					"mary",
					"marytra@gmail.com",
					encoder.encode("mary123")
			);
			user.setRoles(roles);
			userRepository.save(user);
		}

		// CREATION DE L'ADMINISTRATEUR
		if (userRepository.findAll().size() == 1) {
			Set<Role> roles = new HashSet<>();
			Role role = roleRepository.findByName(ERole.ROLE_ADMIN).get();
			roles.add(role);
			Admin admin = new Admin(
					"marytraore",
					"marytra292@gmail.com",
					encoder.encode("mary123"),
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
			Clinique clinique = new Clinique(
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
			clinique.setRoles(roles);
			clinicsRepository.save(clinique);

		}

		// CREATION D'UN PATIENT
		Patients patients = null;
		if (patientRepository.findAll().size() == 0) {
			Set<Role> roles = new HashSet<>();
			Role role = roleRepository.findByName(ERole.ROLE_PATIENT).get();
			roles.add(role);
			patients = new Patients(
					"patient",
					"patient@gmail.com",
					encoder.encode("patient123"),
					"Patient",
					"PATIENT",
					"Homme",
					"1998/02/24",
					"+223 93 77 15 53"
			);
			patients.setRoles(roles);
			this.patientRepository.save(patients);
		}


		//CREATION D'UN MEDECIN
		Medecins medecins = null;
		if (medecinsRepository.findAll().size() == 0) {
			medecins = new Medecins(
					"Medecin",
					"MEDECIN",
					"medecin@gmail.com",
					"Homme",
					"1998/02/24",
					"+223 78 46 75 33"
			);
			this.medecinsRepository.save(medecins);
		}

		// HORAIRE DU CALENDRIER
		Set<Calendrier> calendrierSet = new HashSet<>();
		Calendrier calendrier1 = new Calendrier(1L, LocalTime.of(8, 0), LocalTime.of(9, 0));
		Calendrier calendrier2 = new Calendrier(2L, LocalTime.of(9, 0), LocalTime.of(10, 0));
		Calendrier calendrier3 = new Calendrier(3L, LocalTime.of(10, 0), LocalTime.of(11, 0));
		Calendrier calendrier4 = new Calendrier(4L, LocalTime.of(11, 0), LocalTime.of(12, 0));
		Calendrier calendrier5 = new Calendrier(5L, LocalTime.of(12, 0), LocalTime.of(13, 0));
		Calendrier calendrier6 = new Calendrier(6L, LocalTime.of(13, 0), LocalTime.of(14, 0));
		Calendrier calendrier7 = new Calendrier(7L, LocalTime.of(14, 0), LocalTime.of(15, 0));
		Calendrier calendrier8 = new Calendrier(8L, LocalTime.of(15, 0), LocalTime.of(16, 0));
		Calendrier calendrier9 = new Calendrier(9L, LocalTime.of(16, 0), LocalTime.of(17, 0));
		calendrierSet.add(calendrier1);
		calendrierSet.add(calendrier2);
		calendrierSet.add(calendrier3);
		calendrierSet.add(calendrier4);
		calendrierSet.add(calendrier5);
		calendrierSet.add(calendrier6);
		calendrierSet.add(calendrier7);
		calendrierSet.add(calendrier8);
		calendrierSet.add(calendrier9);
		this.calendrierRepository.saveAll(calendrierSet);


		// CREATION DES RENDEZ-VOUS
//		RendezVous rdv = new RendezVous(
//				medecins,
//				patients,
//				calendrier1,
//				LocalDate.now(),
//				true
//		);

//		Set<RendezVous> RdvSet = new HashSet<>();
//		RendezVous RdvSet1 = new RendezVous(medecins, patients, calendrier1, LocalDate.now(), true);
////		RendezVous RdvSet2 = new RendezVous(medecin, patient, calendrier3, LocalDate.now(), true);
////		RendezVous RdvSet3 = new RendezVous(medecin, patient, calendrier2, LocalDate.now(), true);
////		RendezVous RdvSet4 = new RendezVous(medecin, patient, calendrier5, LocalDate.now(), false);
//		//RdvSet.add(RdvSet1);
////		RdvSet.add(RdvSet2);
////		RdvSet.add(RdvSet3);
////		RdvSet.add(RdvSet4);
//		this.rendezVousRepository.save(RdvSet1);


	}



}
