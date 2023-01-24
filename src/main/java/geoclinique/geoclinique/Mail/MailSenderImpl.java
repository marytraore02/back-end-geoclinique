package geoclinique.geoclinique.Mail;

import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class MailSenderImpl implements IMailSender {

    private JavaMailSenderImpl mailSender;

    public MailSenderImpl(Environment environment) {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(environment.getProperty("spring.mail.hos"));
        mailSender.setPort(Integer.parseInt(environment.getProperty("spring.mail.por")));
        mailSender.setUsername(environment.getProperty("spring.mail.usernam"));
        mailSender.setPassword(environment.getProperty("spring.mail.passwor"));
    }

    //La methode d'envoi de feedback
    @Override
    public void sendFeedback(String from, String name, String feedback) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("marytra17@gmail.com");
        message.setFrom(from);
        message.setSubject("New feedback from " + name);
        message.setText(feedback);
        this.mailSender.send(message);
    }

    @Override
    public void notifyPatient(boolean status, String prenomMdecin, String nomMedecin, String patientMail, LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        SimpleMailMessage message = new SimpleMailMessage();
        var reponse = (status)? "confirmer" : "annuler";
        message.setTo(patientMail);
        message.setFrom("marytra17@gmail.com");
        message.setSubject("Rendez-vous "+ reponse +" par Dr."+ prenomMdecin +" "+ nomMedecin);
        message.setText("Le docteur. "+ prenomMdecin +" "+ nomMedecin+" a " + reponse +
                " votre demande de rendez-vous pour le "+date+" de "+ heureDebut+ " à "+heureFin+".");
        this.mailSender.send(message);
    }

}
