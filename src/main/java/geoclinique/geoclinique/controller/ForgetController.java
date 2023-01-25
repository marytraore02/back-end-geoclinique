package geoclinique.geoclinique.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import geoclinique.geoclinique.dto.Message;
import geoclinique.geoclinique.model.EmailDetails;
import geoclinique.geoclinique.model.ForgetPassword;
import geoclinique.geoclinique.model.Patients;
import geoclinique.geoclinique.model.Utilisateur;
import geoclinique.geoclinique.payload.response.MessageResponse;
import geoclinique.geoclinique.payload.response.ResponseMessage;
import geoclinique.geoclinique.service.EmailService;
import geoclinique.geoclinique.service.ForgetPasswordService;
import geoclinique.geoclinique.service.PatientSevice;
import geoclinique.geoclinique.service.UtilisateurService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@CrossOrigin(origins ={ "http://localhost:4200/", "http://localhost:8100/", "http://localhost:8200/"  }, maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/motdepass")
@Api(value = "formatemail", description = "Les fonctionnalités liées à une formatemail")
public class ForgetController {
    @Autowired
    UtilisateurService utilisateurService;
    @Autowired
    EmailService emailService;
    @Autowired
    private ForgetPasswordService forgetpass;
    @Autowired
    PatientSevice patientSevice;



    @ApiOperation(value = "Send Mail")
    @PostMapping("/forgetpassword")
    public ResponseEntity<Object> SendEmail(@RequestParam(value = "patient") String userVenant) {

//            System.out.println(userVenant);
            Patients patients1 = patientSevice.getByEmail(userVenant);

           // Patients patients = new JsonMapper().readValue(userVenant, Patients.class);
            System.out.println(patients1);

            //Patients patients1 = patientSevice.getByEmail(patients.getEmail()).get();

            if (patients1 != null) {
                String lien = "";
                for (int i = 0; i < 20; i++) {
                    Random random = new Random();

                    char randomizedCharacter = (char) (random.nextInt(26) + 'a');
                    lien = lien + "" + randomizedCharacter;
                }

                try {
                    ForgetPassword forget = new ForgetPassword();

//                    System.out.println(patients.getEmail());
//                    System.out.println(patients1.getEmail());

                    EmailDetails detail = new EmailDetails();
                    detail.setRecipient(patients1.getEmail());
                    detail.setMsgBody(
                            "Vous avez demandez une reinitialisation de mot de passe ! \n Veuillez clicquez sur le lien suivant :\nhttp://localhost:8100/modifierpassword2/"
                                    + lien);
                    System.out.println(emailService.sendSimpleMail(detail));

                    Date date = new Date();
                    forget.setCode(lien);
                    forget.setPatient(patients1);
                    forget.setDate(date);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            forgetpass.Create(forget));

                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Code expiré !");
//                    return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Tentative d'usurpation !!");
            }


    }


    /// ::::::::::::::::::::::::::Liste par id
    @ApiOperation(value = "Send Mail")
    @PostMapping("/change/password/{code}")
    public ResponseEntity<Object> ChangePassword(@RequestParam(value = "patient") String userVenant,
                                                 @PathVariable("code") String code) throws ParseException {

            Patients patients1 = patientSevice.getByEmail(userVenant);
            System.out.println(patients1);

           // Patients patients = new JsonMapper().readValue(userVenant, Patients.class);

            //Patients patients1 = patientSevice.getByEmail(patients.getEmail());

            if (patients1 != null) {
                ForgetPassword forget = forgetpass.Recuperer(code);
                if (forget != null) {
                    if (forget.getPatient() == patients1) {

                        //Conversion du format de date
                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                        //Creation l'instance date
                        Date today = new Date();

                        Date todayWithZeroTime = formatter.parse(formatter.format(today));

                        Date forgetDate = formatter.parse(formatter.format(forget.getDate()));

                        System.out.println("date auj" + todayWithZeroTime);
                        System.out.println("date oub" + forgetDate);

                        if (forgetDate.equals(todayWithZeroTime)) {

                            ///
//                            Historique historique = new Historique();
//                            historique.setDatehistorique(new Date());
//                            historique.setDescription(user.getPrenom() + " " + user.getNom()
//                                    + " a modifie son mot de passe.");
//                            historiqueService.Create(historique);

                            patients1.setPassword(patients1.getPassword());
                            patientSevice.modifier(patients1);
                            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                                    patientSevice.modifier(patients1));
                        } else {
                            return ResponseMessage.generateResponse("error", HttpStatus.OK, "Code expiré !");
                        }

                    } else {
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, "Tentative d'usurpation !!");
                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Erreur de code!");
                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");
            }

    }


}
