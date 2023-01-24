package geoclinique.geoclinique.service;

import geoclinique.geoclinique.Api.DtoMapper.RendezVousMapper;
import geoclinique.geoclinique.model.*;
import geoclinique.geoclinique.repository.RendezVousRepository;
import geoclinique.geoclinique.Api.DtoViewModel.Response.DisponibiliteClinicResponse;
import geoclinique.geoclinique.Api.DtoViewModel.Request.DisponibiliteClinicRequest;
import geoclinique.geoclinique.configuration.EmailConstructor;
import geoclinique.geoclinique.repository.ClinicsRepository;
import geoclinique.geoclinique.repository.RoleRepository;
import geoclinique.geoclinique.repository.UserRepository;
import geoclinique.geoclinique.util.TweakResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClinicsServices {
    @Autowired
    ClinicsRepository clinicsRepository;
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

    public Clinics creer(Clinics clinics){
        clinics.setPassword(encoder.encode(clinics.getPassword()));
        return clinicsRepository.save(clinics);
    }
    public List<Clinics> read(){
        return clinicsRepository.findAll();
    }
    public Optional<Clinics> GetOne(Long id){
        return clinicsRepository.findById(id);
    }
    public boolean existsByName(String name){
        return clinicsRepository.existsByNomClinic(name);
    }
    public boolean existsByUsername(String username){
        return clinicsRepository.existsByUsername(username);
    }
    public boolean existsById(Long id){
        return clinicsRepository.existsById(id);
    }
    public Optional<Clinics> getByNomClinic(String name){
        return clinicsRepository.findByNomClinic(name);
    }
    public Clinics modifier(Long id, Clinics clinics) {
        return clinicsRepository.findById(id)
                .map(p->{
                    p.setUsername(clinics.getUsername());
                    p.setEmail(clinics.getEmail());
                    p.setPassword(encoder.encode(clinics.getPassword()));
                    p.setNomClinic(clinics.getNomClinic());
                    p.setDescriptionClinic(clinics.getDescriptionClinic());
                    p.setAdresseClinic(clinics.getAdresseClinic());
                    p.setVilleClinic(clinics.getVilleClinic());
                    p.setLongitudeClinic(clinics.getLongitudeClinic());
                    p.setLatitudeClinic(clinics.getLatitudeClinic());
                    p.setContactClinic(clinics.getContactClinic());
//                    p.setStatusClinic(clinics.isStatusClinic());
                    return clinicsRepository.save(p);
                }).orElseThrow(()-> new RuntimeException("Clinics non trouvé !"));
    }
    public String delete(Long id){
        clinicsRepository.deleteById(id);
        return "Supprimer avec succes";
    }




}
