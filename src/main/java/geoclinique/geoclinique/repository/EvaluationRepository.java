package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
        Evaluation findByMessage(String evaluation);

}
