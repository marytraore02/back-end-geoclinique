package geoclinique.geoclinique.configuration;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ImageConfig {

    public static String localhost = "http://localhost/";

    //Stockage photo de la clinique
    public static String serverclinique = localhost + "geoclinique/images/cliniques/";
    public static String Cliniquelocation = "/Applications/XAMPP/xamppfiles/htdocs/geoclinique/images/cliniques/";

    //Stockage photo de l'agrement de la clinique
    public static String serveragrementclinique = localhost + "geoclinique/images/agrements-clinique/";
    public static String Agrementcliniquelocation = "/Applications/XAMPP/xamppfiles/htdocs/geoclinique/images/agrements-clinique/";

    //Stockage photo medecin
    public static String servermedecin = localhost + "geoclinique/images/medecins/";
    public static String Medecinlocation = "/Applications/XAMPP/xamppfiles/htdocs/geoclinique/images/medecins/";

    //Stockage photo specialite
    public static String serverspecialite = localhost + "geoclinique/images/specialites/";
    public static String Specialitelocation = "/Applications/XAMPP/xamppfiles/htdocs/geoclinique/images/specialites/";

    //Stockage photo patient
    public static String serverspatient = localhost + "geoclinique/images/patients/";
    public static String Patientlocation = "/Applications/XAMPP/xamppfiles/htdocs/geoclinique/images/patients/";


    public static String save(String typeImage, MultipartFile file, String nomFichier) {
        String src = "";
        String server = "";
        String location = "";
        if (typeImage == "medecin") {
            location = Medecinlocation;
            server = servermedecin;
        } else if (typeImage == "agrementclinique"){
            location = Agrementcliniquelocation;
            server = serveragrementclinique;
        } else if (typeImage == "clinique"){
            location = Cliniquelocation;
            server = serverclinique;
        } else if (typeImage == "specialite"){
            location = Specialitelocation;
            server = serverspecialite;
        } else {
            location = Patientlocation;
            server = serverspatient;
        }

        /// debut de l'enregistrement
        try {
            int index = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");

            Path chemin = Paths.get(location);
            if (!Files.exists(chemin)) {
                // si le fichier n'existe pas deja
                Files.createDirectories(chemin);
                Files.copy(file.getInputStream(), chemin
                        .resolve(nomFichier + file.getOriginalFilename().substring(index).toLowerCase()));
                src = server + nomFichier
                        + file.getOriginalFilename().substring(index).toLowerCase();
            } else {
                // si le fichier existe pas deja
                String newPath = location + nomFichier
                        + file.getOriginalFilename().substring(index).toLowerCase();
                Path newchemin = Paths.get(newPath);
                if (!Files.exists(newchemin)) {
                    // si le fichier n'existe pas deja
                    Files.copy(file.getInputStream(), chemin
                            .resolve(
                                    nomFichier + file.getOriginalFilename().substring(index).toLowerCase()));
                    src = server + nomFichier
                            + file.getOriginalFilename().substring(index).toLowerCase();
                } else {
                    // si le fichier existe pas deja on le suprime et le recrèe
                    Files.delete(newchemin);

                    Files.copy(file.getInputStream(), chemin
                            .resolve(
                                    nomFichier + file.getOriginalFilename().substring(index).toLowerCase()));
                    src = server + nomFichier
                            + file.getOriginalFilename().substring(index).toLowerCase();
                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            // TODO: handle exception
            src = null;
        }

        return src;
    }

}
