package geoclinique.geoclinique.service;

import geoclinique.geoclinique.model.ForgetPassword;
import geoclinique.geoclinique.repository.ForgetPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForgetPasswordService {
    @Autowired
    ForgetPasswordRepository forgotPasswordRepository;

    public ForgetPassword Create(ForgetPassword forgetPass) {
        return forgotPasswordRepository.save(forgetPass);
    }


    public ForgetPassword Recuperer(String code) {
        // TODO Auto-generated method stub
        return forgotPasswordRepository.findByCode(code);
    }
}
