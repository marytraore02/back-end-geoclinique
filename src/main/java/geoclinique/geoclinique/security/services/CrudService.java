package geoclinique.geoclinique.security.services;

import geoclinique.geoclinique.model.Utilisateur;

        import java.util.List;

public interface CrudService {

    String Supprimer(Long id_users);  // LA METHODE PERMETTANT DE SUPPRIMER UN COLLABORATEUR

    String Modifier(Utilisateur users);   // LA METHODE PERMETTANT DE MODIFIER UN COLLABORATEUR

    List<Utilisateur> Afficher();       // LA METHODE PERMETTANT D'AFFICHER UN COLLABORATEUR

}