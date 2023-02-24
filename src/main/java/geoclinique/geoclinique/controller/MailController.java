package geoclinique.geoclinique.controller;

import geoclinique.geoclinique.Api.DtoViewModel.Request.EvaluationRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Request.FeedbackRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Response.ApiResponse;
import geoclinique.geoclinique.Mail.IMailSender;
import geoclinique.geoclinique.configuration.EmailConstructor;
import geoclinique.geoclinique.dto.Message;
import geoclinique.geoclinique.model.Evaluation;
import geoclinique.geoclinique.model.FeedBack;
import geoclinique.geoclinique.model.Medecins;
import geoclinique.geoclinique.model.RendezVous;
import geoclinique.geoclinique.repository.*;
import geoclinique.geoclinique.security.CurrentUser;
import geoclinique.geoclinique.security.services.UserDetailsImpl;
import geoclinique.geoclinique.service.RendezVousService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins ={ "http://localhost:4200/", "http://localhost:8100/", "http://localhost:8200/"  }, maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/patient/mail")
public class MailController {

    private UserRepository userRepository;
    private IMailSender emailSender;
    @Autowired
    RendezVousRepository rendezVousRepository;
    @Autowired
    MedecinsRepository medecinsRepository;
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    RendezVousService rendezVousService;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    CliniqueRepository cliniqueRepository;
    @Autowired
    EvaluationRepository evaluationRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailConstructor emailConstructor;

    public MailController(UserRepository userRepository, IMailSender mailSender) {
        this.userRepository = userRepository;
        this.emailSender = mailSender;
    }

    @ApiOperation(value = "Afficher les feedack")
    @GetMapping("/read")
    public ResponseEntity<List<FeedBack>> Afficher(){
        List<FeedBack> feedBacks = feedbackRepository.findAll();
        return new ResponseEntity(feedBacks, HttpStatus.OK);
    }


    @PostMapping("/feedback")
    public ResponseEntity<?> sendFeedback(@RequestBody FeedbackRequest feedback, BindingResult bindingResult){
        var name = String.valueOf(feedback.getName());
        var email = String.valueOf(feedback.getEmail());
        //var date = LocalDate.parse(feedback.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        var date = new Date();
        var contenue = String.valueOf(feedback.getContenue());
//        System.err.println(name);
//        System.err.println(email);
//        System.err.println(contenue);
        try{
            FeedBack feedBack = new FeedBack(name,email,feedback.getContenue(),date);
            this.feedbackRepository.save(feedBack);
            // Send Email
            mailSender.send(emailConstructor.Feedback(feedback.getEmail(), feedback.getName(), feedback.getContenue()));
            return ResponseEntity.ok(
                    new ApiResponse(true, "Feedback envoyer avec success.")
            );
            
        } catch (Exception e){
            return new ResponseEntity(new Message("Erreur lors de l'envoi du message"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/evaluer")
    public ResponseEntity<?> EvaluationRdv(@RequestBody EvaluationRequest evaluationRequest, BindingResult bindingResult){

        var rdv = this.rendezVousRepository.findById(evaluationRequest.getRendezvousId());
        var date = LocalDate.parse(evaluationRequest.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        var message = String.valueOf(evaluationRequest.getMessage());
        System.err.println(evaluationRequest.getMessage());

        if(rdv.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Le rendez-vous n'existe pas."),
                    HttpStatus.NOT_FOUND);
        }

        try{
            Evaluation evaluation = new Evaluation(evaluationRequest.getMessage(),rdv.get(), date);
            this.evaluationRepository.save(evaluation);
            // Send Email
            //mailSender.send(emailConstructor.Feedback(feedback.getEmail(), feedback.getName(), feedback.getFeedback()));
            return ResponseEntity.ok(
                    new ApiResponse(true, "Evaluation envoyer avec success.")
            );

        } catch (Exception e){
            return new ResponseEntity(new Message("Erreur lors de l'envoi du message"), HttpStatus.BAD_REQUEST);
        }
    }
}
