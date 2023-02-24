package geoclinique.geoclinique.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import geoclinique.geoclinique.configuration.ImageConfig;
import geoclinique.geoclinique.dto.Message;
import geoclinique.geoclinique.model.Clinique;
import geoclinique.geoclinique.model.Medecins;
import geoclinique.geoclinique.model.RendezVous;
import geoclinique.geoclinique.model.Specialites;
import geoclinique.geoclinique.repository.CliniqueRepository;
import geoclinique.geoclinique.repository.MedecinsRepository;
import geoclinique.geoclinique.repository.RendezVousRepository;
import geoclinique.geoclinique.repository.SpecialiteRepository;
import geoclinique.geoclinique.service.CliniqueServices;
import geoclinique.geoclinique.service.MedecinsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins ={ "http://localhost:4200/", "http://localhost:8100/", "http://localhost:8200/"  }, maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/medecin")
@Api(value = "hello", description = "CRUD MEDECIN")
public class MedecinController {
    @Autowired
    MedecinsRepository medecinsRepository;
    @Autowired
    CliniqueRepository cliniqueRepository;
    @Autowired
    RendezVousRepository rendezVousRepository;
    @Autowired
    CliniqueServices cliniqueServices;
    @Autowired
    MedecinsService medecinsService;
    @Autowired
    SpecialiteRepository specialiteRepository;

    //@PreAuthorize("hasRole('ADMIN') or hasRole('CLINIC')")
    @ApiOperation(value = "Creation d'un medecin")
        @PostMapping("/create/new/{idCli}/{idSpec}")
    public ResponseEntity<?> create(@RequestParam(value = "data") String acti,
                                    @PathVariable("idCli") Long idCli,
                                    @PathVariable("idSpec") Long idSpec,
                                    @RequestParam(value = "file", required = false) MultipartFile file)

            throws IOException {
        Medecins medecins = null;
        try {
            medecins = new JsonMapper().readValue(acti, Medecins.class);
            System.out.println(medecins);

            Clinique clinique = cliniqueServices.GetOne(idCli).get();
            Specialites specialites = specialiteRepository.getReferenceById(idSpec);

            if(StringUtils.isBlank(medecins.getNomMedecin()))
                return new ResponseEntity(new Message("Le nom du medecin est obligatoire"), HttpStatus.BAD_REQUEST);

            if(medecinsService.existeByEmailMedecin(medecins.getEmailMedecin()))
                return new ResponseEntity(new Message("Ce email existe déjà"), HttpStatus.BAD_REQUEST);

            if(medecinsService.existByContact(medecins.getContactMedecin()))
                return new ResponseEntity(new Message("Le numero existe déjà"), HttpStatus.BAD_REQUEST);

            if (file != null) {
                try {
                    //Verification de l'existance de l'id de la clinic
                    if(!cliniqueServices.existsById(idCli))
                        return new ResponseEntity(new Message("La clinique n'existe pas"), HttpStatus.NOT_FOUND);
                    if(!specialiteRepository.existsById(idSpec))
                        return new ResponseEntity(new Message("La specialiite n'existe pas"), HttpStatus.NOT_FOUND);
                    medecins.setImageMedecin(ImageConfig.save("medecin", file, medecins.getPrenomMedecin()));
                    medecins.getListeSpecialiteMed().add(specialites);
                    medecins.setClinique(clinique);
                    medecinsService.creer(medecins);
                    return new ResponseEntity(new Message("Medecins créé avec success"), HttpStatus.OK);


                } catch (Exception e) {
                    // TODO: handle exception
                    return new ResponseEntity(new Message("Erreur de creation"), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity(new Message("Photo introuvable"), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(medecins);
            return new ResponseEntity( e.getMessage(), HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Afficher medecin")
    @GetMapping("/read")
    public ResponseEntity<List<Medecins>> Afficher(){
        List<Medecins> medecins = medecinsService.read();
        return new ResponseEntity(medecins, HttpStatus.OK);
    }

    @ApiOperation(value = "Metter à jour medecin")

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@RequestParam(value = "data") String acti,
                                         @PathVariable("id") Long id,
                                         @RequestParam(value = "file", required = false) MultipartFile file)
            throws IOException {
        Medecins medecins = null;

        try {
            medecins = new JsonMapper().readValue(acti, Medecins.class);
//            Random e = new Random();
//            e.nextInt(8);
            if (file != null) {
                medecins.setImageMedecin(ImageConfig.save("medecin", file, medecins.getPrenomMedecin()));
            }
            medecinsService.modifier(id, medecins);
            return new ResponseEntity(new Message("Medecin modifie"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Message("Erreur de modification"), HttpStatus.OK);
        }
    }


    @ApiOperation(value = "Suppression medecin")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> supprimer(@PathVariable long id) {
        try {
            if(!medecinsService.existsById(id))
                return new ResponseEntity(new Message("Id n'existe pas"), HttpStatus.NOT_FOUND);
            medecinsService.delete(id);
            return new ResponseEntity(new Message("Medecin supprimer avec succès"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Message("Erreur de creation"), HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Affichager un medecin")
    @GetMapping("/get/{id}")
    public ResponseEntity<Medecins> getById(@PathVariable("id") Long id){
        if(!medecinsService.existsById(id))
            return new ResponseEntity(new Message("Medecin n'existe pas"), HttpStatus.NOT_FOUND);
        Medecins medecins = medecinsService.getOne(id);
        return new ResponseEntity(medecins, HttpStatus.OK);
    }

    @ApiOperation(value = "Affichager les medecins d'une clinique")
    @GetMapping("/getMedecin/{id}")
    public ResponseEntity<List<Medecins>> getByMedecin(@PathVariable("id") Long id){
        if(!cliniqueRepository.existsById(id))
            return new ResponseEntity(new Message("Clinique n'existe pas"), HttpStatus.NOT_FOUND);
        Clinique clinique = cliniqueRepository.findById(id).get();
        List<Medecins> medecins = medecinsRepository.findByClinique(clinique);
        return new ResponseEntity(medecins, HttpStatus.OK);
    }



}
