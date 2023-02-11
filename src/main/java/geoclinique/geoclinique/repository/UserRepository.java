package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.Role;
import geoclinique.geoclinique.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByUsername(String username);

    Boolean existsByUsername(String username);
    Boolean existsByNomEtPrenom(String nom);

    Boolean existsByEmail(String email);

    Utilisateur findByEmail(String email);

    Utilisateur findByEmailAndPassword(String email, String password);


    List<Utilisateur> findById(Role role);
}
