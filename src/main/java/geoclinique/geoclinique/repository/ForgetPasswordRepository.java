package geoclinique.geoclinique.repository;

import geoclinique.geoclinique.model.ForgetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword, Long> {
    ForgetPassword findByCode(String code);
}
