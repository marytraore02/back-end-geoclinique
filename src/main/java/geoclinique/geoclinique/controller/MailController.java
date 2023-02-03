package geoclinique.geoclinique.controller;

import geoclinique.geoclinique.Api.DtoViewModel.Request.FeedbackRequest;
import geoclinique.geoclinique.Api.DtoViewModel.Response.ApiResponse;
import geoclinique.geoclinique.Mail.IMailSender;
import geoclinique.geoclinique.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/patient/mail")
@CrossOrigin
public class MailController {

    private UserRepository userRepository;
    private IMailSender mailSender;

    public MailController(UserRepository userRepository, IMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> sendFeedback(@RequestBody FeedbackRequest feedback, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bad request."
            );
        }
        this.mailSender.sendFeedback(feedback.getEmail(), feedback.getName(), feedback.getFeedback());
        return ResponseEntity.ok(
                new ApiResponse(true, "Feedback envoyer avec success.")
        );
    }

}
