package geoclinique.geoclinique.service;

import geoclinique.geoclinique.configuration.EmailConstructor;
import geoclinique.geoclinique.model.Clinics;
import geoclinique.geoclinique.model.Utilisateur;
import geoclinique.geoclinique.model.Role;
import geoclinique.geoclinique.repository.ClinicsRepository;
import geoclinique.geoclinique.repository.RoleRepository;
import geoclinique.geoclinique.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

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

    public List<Clinics> read(){
        return clinicsRepository.findAll();
    }

    public Utilisateur getByEmail(String email) {
        // TODO Auto-generated method stub
        return userRepository.findByEmail(email);
    }

    public Utilisateur creer(Utilisateur utilisateur){
        return userRepository.save(utilisateur);
    }

    public Role create(Role role) {
        return roleRepository.save(role);
    }
}
