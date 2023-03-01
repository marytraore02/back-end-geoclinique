package geoclinique.geoclinique;

//import geoclinique.geoclinique.configuration.JacksonConfig;
import geoclinique.geoclinique.configuration.ImageConfig;
import geoclinique.geoclinique.model.*;
import geoclinique.geoclinique.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import geoclinique.geoclinique.model.Utilisateur;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@SpringBootApplication
//@Import(JacksonConfig.class)
public class GeoCliniqueApplication implements CommandLineRunner{
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	MotifRepository motifRepository;
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

	@Override
	public void run(String... args) throws Exception {

//		adminRepository.deleteAll();
//		calendrierRepository.deleteAll();
//		clinicsRepository.deleteAll();
//		medecinsRepository.deleteAll();
//		patientRepository.deleteAll();
//		rendezVousRepository.deleteAll();
//		//roleRepository.deleteAll();
//		specialiteRepository.deleteAll();
//		userRepository.deleteAll();

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
					"Mary TRAORE",
					"+223 93 77 15 53",
					"1998/02/24",
					"Image",
					"mary",
					"marytra17@gmail.com",
					encoder.encode("mary123")
			);
			user.setRoles(roles);
			userRepository.save(user);
		}



		// CREATION DES SPECIALITES
			Set<Specialites> specialites = new HashSet<>();
			Specialites specialites1 = new Specialites("Santé Publique ", "Description de la sante publique");
			Specialites specialites2 = new Specialites("Médecine générale", "Description du medecin generaliste");
			Specialites specialites3 = new Specialites("Pédiatrie", "Description du pediatre");
			Specialites specialites4 = new Specialites("Radiologie", "Description du radiologue");
			Specialites specialites5 = new Specialites("Dentiste", "Description du dentiste");
			specialites.add(specialites1);
			specialites.add(specialites2);
			specialites.add(specialites3);
			specialites.add(specialites4);
			specialites.add(specialites5);
			this.specialiteRepository.saveAll(specialites);


		// CREATION D'UNE CLINIQUE
			Set<Role> roles = new HashSet<>();
			Role role = roleRepository.findByName(ERole.ROLE_CLINIC).get();
			roles.add(role);

			List<Specialites> specialite = new ArrayList<>();
			Specialites sp3 = specialites3;
			Specialites sp4 = specialites4;
			specialite.add(sp3);
			specialite.add(sp4);
			Clinique clinique = new Clinique(
					"Clinique pastère",
					"+223 93 77 15 53",
					"1998/02/24",
					"clinique",
					"clinique@gmail.com",
					encoder.encode("clinique123"),
					"Une court description de la que pastere",
					"Bamako",
					"Bamako Hamdalaye ACI 2000",
					12.6370293,
					-8.0335354

			);
//			MultipartFile file = null;
//			clinique.setAgrementClinique(ImageConfig.save("agrementclinique", file, clinique.getNomEtPrenom()));
//			clinique.setImage(ImageConfig.save("clinique", fil, clinique.getNomEtPrenom()));

			clinique.setStatusClinique(true);
			clinique.setRoles(roles);
			clinique.setListeSpecialiteCli(specialite);
			clinicsRepository.save(clinique);



		// CREATION D'UN PATIENT
		Patients patients = null;
		if (patientRepository.findAll().size() == 0) {
			Set<Role> roles1 = new HashSet<>();
			Role rol = roleRepository.findByName(ERole.ROLE_PATIENT).get();
				roles1.add(rol);
			patients = new Patients(
					"patient PATIENT",
					"+223 93 77 15 53",
					"16/10/2022",
					"patient",
					"marytra292@gmail.com",
					encoder.encode("patient123"),
					"Homme"
			);
			patients.setRoles(roles1);
			this.patientRepository.save(patients);
		}


		//CREATION D'UN MEDECIN
		Medecins medecins = null;
		if (medecinsRepository.findAll().size() == 0) {
			List<Specialites> specialit = new ArrayList<>();
			Specialites sp1 = specialites1;
			specialit.add(sp1);
			medecins = new Medecins(
					"Medecin",
					"MEDECIN",
					"medecin@gmail.com",
					"Homme",
					"1998/02/24",
					"+223 78 46 75 33",
					clinique
			);
			medecins.setListeSpecialiteMed(specialit);
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



//
//		// CREATION DE MOTIFS
			Set<Motif> Motif = new HashSet<>();
			Motif Motif1 = new Motif(1L, "Consultation");
			Motif Motif2 = new Motif(2L, "Contrôle");
			Motif Motif3 = new Motif(3L, "Urgence");
			Motif Motif4 = new Motif(4L, "Devis");
			Motif Motif5 = new Motif(5L, "Autre");
			Motif.add(Motif1);
			Motif.add(Motif2);
			Motif.add(Motif3);
			Motif.add(Motif4);
			Motif.add(Motif5);
			this.motifRepository.saveAll(Motif);


		// CREATION DE RENDEZ VOUS
		if(rendezVousRepository.findAll().size() == 0){
			Set<RendezVous> RdvSet = new HashSet<>();
			RendezVous RdvSet1 = new RendezVous(Motif1, medecins, patients, calendrier1, LocalDate.now(), true);
			RendezVous RdvSet2 = new RendezVous(Motif2, medecins, patients, calendrier3, LocalDate.now(), false);
			RdvSet.add(RdvSet1);
			RdvSet.add(RdvSet2);
			this.rendezVousRepository.saveAll(RdvSet);
		}






	}



}
