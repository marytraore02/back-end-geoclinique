package geoclinique.geoclinique.configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
public class ImgConf {
    public static void saveimg(String uploaDir, String nomfile, MultipartFile multipartFile) throws IOException {

        Path UploadPath = Paths.get(uploaDir);

        if(!Files.exists(UploadPath)) {
            Files.createDirectories(UploadPath);
        }
        try(InputStream inputStream = multipartFile.getInputStream()){
            Path fichierPath = UploadPath.resolve(nomfile);

            Files.copy(inputStream, fichierPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe){
            throw new IOException("Impossible d'enregistrer le fichier image:" + nomfile, ioe);
        }
    }
}
