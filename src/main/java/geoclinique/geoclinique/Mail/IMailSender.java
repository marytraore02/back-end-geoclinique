package geoclinique.geoclinique.Mail;

import java.time.LocalDate;
import java.time.LocalTime;

public interface IMailSender {
    void sendFeedback(String from, String name, String feedback);
    void notifyPatient(boolean status, String prenom, String nom, String patientMail, LocalDate date, LocalTime heureDebut, LocalTime heureFin);

}
