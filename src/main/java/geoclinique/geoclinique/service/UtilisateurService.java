package geoclinique.geoclinique.service;

import geoclinique.geoclinique.model.Utilisateur;
import geoclinique.geoclinique.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {

    @Autowired
    UserRepository userRepository;
    public Utilisateur getByEmail(String email) {
        // TODO Auto-generated method stub
        return userRepository.findByEmail(email);
    }
    public Utilisateur creer(Utilisateur utilisateur){
        return userRepository.save(utilisateur);
    }


}