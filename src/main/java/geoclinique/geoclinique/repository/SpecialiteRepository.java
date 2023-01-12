package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.Specialites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialiteRepository extends JpaRepository<Specialites, Long> {
        Optional<Specialites>  findByLibelleSpecialite(String name);
        boolean existsByLibelleSpecialite(String name);

}
