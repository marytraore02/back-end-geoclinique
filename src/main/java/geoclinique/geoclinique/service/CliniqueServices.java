package geoclinique.geoclinique.service;

import geoclinique.geoclinique.Api.DtoMapper.RendezVousMapper;
import geoclinique.geoclinique.Api.DtoViewModel.Request.RdvMedecinRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Request.TodayRdvRequest;
import geoclinique.geoclinique.model.*;
import geoclinique.geoclinique.configuration.EmailConstructor;
import geoclinique.geoclinique.repository.*;
import geoclinique.geoclinique.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import geoclinique.geoclinique.Api.DtoViewModel.Response.ApiResponse;
import org.springframework.http.HttpStatus;



import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class CliniqueServices {
    @Autowired
    CliniqueRepository clinicsRepository;
    @Autowired
    MedecinsRepository medecinsRepository;
    @Autowired
    RendezVousRepository rendezVousRepository;
    @Autowired
    RendezVousMapper rendezVousMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private EmailConstructor emailConstructor;

    @Autowired
    private JavaMailSender mailSender;



    public void resetPassword(Utilisateur user) {
        String password = RandomStringUtils.randomAlphanumeric(10);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        mailSender.send(emailConstructor.constructResetPasswordEmail(user, password));

    }

    public Clinique creer(Clinique clinique){
        clinique.setPassword(encoder.encode(clinique.getPassword()));
        return clinicsRepository.save(clinique);
    }
    public List<Clinique> read(){
        return clinicsRepository.findAll();
    }
    public Optional<Clinique> GetOne(Long id){
        return clinicsRepository.findById(id);
    }
//    public boolean existsByName(String name){
//        return clinicsRepository.existsByNomClinique(name);
//    }
    public boolean existsByUsername(String username){
        return clinicsRepository.existsByUsername(username);
    }
    public boolean existsById(Long id){
        return clinicsRepository.existsById(id);
    }

    public boolean existsByEmail(String email){
        return clinicsRepository.existsByEmail(email);
    }
    public Clinique modifier(Long id, Clinique clinique) {
        return clinicsRepository.findById(id)
                .map(p->{
                    p.setNomEtPrenom((clinique.getNomEtPrenom()));
                    p.setContact(clinique.getContact());
                    p.setDate(clinique.getDate());
                    p.setUsername(clinique.getUsername());
                    p.setEmail(clinique.getEmail());
                    p.setPassword(encoder.encode(clinique.getPassword()));
                    p.setDescriptionClinique(clinique.getDescriptionClinique());
                    p.setAdresseClinique(clinique.getAdresseClinique());
                    p.setVilleClinique(clinique.getVilleClinique());
                    p.setLongitudeClinique(clinique.getLongitudeClinique());
                    p.setLatitudeClinique(clinique.getLatitudeClinique());
//                    p.setStatusClinic(clinique.isStatusClinic());
                    return clinicsRepository.save(p);
                }).orElseThrow(()-> new RuntimeException("Clinique non trouvé !"));
    }
    public String delete(Long id){
        clinicsRepository.deleteById(id);
        return "Supprimer avec succes";
    }


    // Afficher la liste des RDV quotidiens d'un médecin (privé car il contient des données privées sur le patient)
    public Object AllRdvMedecin(UserDetailsImpl currentUser, @RequestBody TodayRdvRequest RdvMedecin){
        var medecin = this.medecinsRepository.findById(currentUser.getId());
        var date = LocalDate.parse(RdvMedecin.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if(medecin.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Medecin not found."),
                    HttpStatus.NOT_FOUND);
        }
        var isAvailable = this.rendezVousRepository.findAllByMedecinsAndDate(medecin.get(), date);
        if(isAvailable.isEmpty()){
            return null;
        }
        int size = isAvailable.size();
        var response = this.rendezVousMapper.toDto(isAvailable, isAvailable.get(0), size);
        return response;
    }


    // Delete RDV.
    public Object deleteEvent(String rdvId){
        var rdv = this.rendezVousRepository.findById(Long.parseLong(rdvId));
        if(rdv.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Le rendez-vous n'exist pas not found."),
                    HttpStatus.NOT_FOUND);
        }
        this.rendezVousRepository.deleteById(Long.parseLong(rdvId));
        // Send Email
//        this.mailSender.notifyPatient(false, appointment.get().getMedecin().getFullName() ,appointment.get().getPatient().getEmail(), appointment.get().getDate(), appointment.get().getShiftHoraire().getTimeStart(), appointment.get().getShiftHoraire().getTimeEnd());

        return new ResponseEntity<>(new ApiResponse(true,"Rendez-vous supprimer avec success"),
                HttpStatus.OK);
    }


    // Modifier status de RDV :
    public Object changeEventStatus(String rdvId){
        var rdv = this.rendezVousRepository.findById(Long.parseLong(rdvId));

        if(rdv.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Rendez vous not found."),
                    HttpStatus.NOT_FOUND);
        }
        var newRdvStatus = !rdv.get().isActive();
        rdv.get().setActive(newRdvStatus);

        this.rendezVousRepository.save(rdv.get());

        // Send Email
//        this.mailSender.notifyPatient(newAppointmentStatus, appointment.get().getMedecin().getFullName() ,appointment.get().getPatient().getEmail(), appointment.get().getDate(), appointment.get().getShiftHoraire().getTimeStart(), appointment.get().getShiftHoraire().getTimeEnd());
        return new ResponseEntity<>(new ApiResponse(true,"Rendez-vous modifier avec success"),
                HttpStatus.OK);
    }

}
