package geoclinique.geoclinique.configuration;


        import geoclinique.geoclinique.model.Utilisateur;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.core.env.Environment;
        import org.springframework.mail.SimpleMailMessage;
        import org.springframework.mail.javamail.MimeMessageHelper;
        import org.springframework.mail.javamail.MimeMessagePreparator;
        import org.springframework.stereotype.Component;
        import org.thymeleaf.TemplateEngine;
        import org.thymeleaf.context.Context;

        import javax.mail.MessagingException;
        import javax.mail.internet.InternetAddress;
        import javax.mail.internet.MimeMessage;
        import java.time.LocalDate;
        import java.time.LocalTime;


@Component
public class EmailConstructor {
    @Autowired
    private Environment env;

    @Autowired
    private TemplateEngine templateEngine;

    public MimeMessagePreparator constructNewUserEmail(Utilisateur user) {
        Context context = new Context();
        context.setVariable("user", user);
        String text = templateEngine.process("newUserEmailTemplate", context);
        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setPriority(1);
                email.setTo(user.getEmail());
                email.setSubject("Confirmation d'inscription - Compte en cours de validation");
                email.setText(text, true);
                email.setFrom(new InternetAddress(env.getProperty("support.email")));
            }
        };
        return messagePreparator;
    }
    public MimeMessagePreparator NotificationPatient(boolean status, String prenomMdecin,
                                                     String nomMedecin, String PatientEmail,
                                                     LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
//        Utilisateur user,
//            Context context = new Context();
//            context.setVariable("user", user);
            MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
                @Override
                public void prepare(MimeMessage mimeMessage) throws Exception {
                    MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                    var reponse = (status)? "confirmer" : "annuler";
                    email.setPriority(1);
                    email.setTo(PatientEmail);
                    email.setFrom(new InternetAddress(env.getProperty("support.email")));
                    email.setSubject("Rendez-vous "+ reponse +" par Dr."+ prenomMdecin +" "+ nomMedecin);
                    email.setText("Le medecin. "+ prenomMdecin +" "+ nomMedecin+" a " + reponse +
                            " votre demande de rendez-vous pour le "+date+" de "+ heureDebut+ " à "+heureFin+".");
            }
        };
        return messagePreparator;
    }

    public MimeMessagePreparator Feedback(String from, String name, String feedback) {
        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setPriority(1);
                email.setFrom(from);
                email.setTo("geoclinique2022@gmail.com");
                email.setSubject("Nouveau feedback envoyer par " + name);
                email.setText(feedback);
            }
        };
        return messagePreparator;
    }

    public MimeMessagePreparator Evaluation(String from, String name, String feedback) {
        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setPriority(1);
                email.setFrom(from);
                email.setTo("geoclinique2022@gmail.com");
                email.setSubject("Nouveau feedback envoyer par " + name);
                email.setText(feedback);
            }
        };
        return messagePreparator;
    }
    public MimeMessagePreparator constructValidationCompteEmail(Utilisateur user) {
        Context context = new Context();
        context.setVariable("user", user);
        String text = templateEngine.process("validationCompteClinique", context);
        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setPriority(1);
                email.setTo(user.getEmail());
                email.setSubject("Confirmation de validation de votre compte");
                email.setText(text, true);
                email.setFrom(new InternetAddress(env.getProperty("support.email")));
            }
        };
        return messagePreparator;
    }


    public MimeMessagePreparator constructResetPasswordEmail(Utilisateur user, String password) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("password", password);
        String text = templateEngine.process("resetPasswordEmailTemplate", context);
        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setPriority(1);
                email.setTo(user.getEmail());
                email.setSubject("New Password - Orchard");
                email.setText(text, true);
                email.setFrom(new InternetAddress(env.getProperty("support.email")));
            }
        };
        return messagePreparator;
    }

    public MimeMessagePreparator constructUpdateUserProfileEmail(Utilisateur user) {
        Context context = new Context();
        context.setVariable("user", user);
        String text = templateEngine.process("updateUserProfileEmailTemplate", context);
        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setPriority(1);
                email.setTo(user.getEmail());
                email.setSubject("Profile Update - Orchard");
                email.setText(text, true);
                email.setFrom(new InternetAddress(env.getProperty("support.email")));
            }
        };
        return messagePreparator;
    }





}
