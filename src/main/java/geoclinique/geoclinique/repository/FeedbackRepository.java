package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedBack, Long> {
}
