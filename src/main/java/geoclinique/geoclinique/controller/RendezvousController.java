package geoclinique.geoclinique.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import geoclinique.geoclinique.configuration.ImageConfig;
import geoclinique.geoclinique.dto.Message;
import geoclinique.geoclinique.model.*;
import geoclinique.geoclinique.repository.PatientRepository;
import geoclinique.geoclinique.repository.RendezVousRepository;
import geoclinique.geoclinique.service.RendezVousService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/rendezvous")
@Api(value = "hello", description = "CRUD RENDEZ-VOUS")
public class RendezvousController {
    @Autowired
    RendezVousRepository rendezVousRepository;
    @Autowired
    RendezVousService rendezVousService;
    @Autowired
    PatientRepository patientRepository;

    //@PreAuthorize("hasRole('PATIENT')")
    @ApiOperation(value = "Afficher la liste des rendez-vous")
    @GetMapping("/read")
    public List<RendezVous> Read(){
        List<RendezVous> rdv = rendezVousService.read();
        return rdv;
    }
//    @ApiOperation(value = "Afficher la liste des rendez-vous par status")
//    @GetMapping("/read/confirmer")
//    public List<RendezVous> ReadConfirmer(){
//        List<RendezVous> rdv = rendezVousService.readRdvByStatus();
//        return rdv;
//    }

    @ApiOperation(value = "Afficher un rendez-vous")
    @GetMapping("/get/{id}")
    public ResponseEntity<RendezVous> getById(@PathVariable("id") Long id){
        if(!rendezVousRepository.existsById(id))
            return new ResponseEntity(new Message("Id n'existe pas"), HttpStatus.NOT_FOUND);
        RendezVous rendezVous = rendezVousService.GetOne(id).get();
        return new ResponseEntity(rendezVous, HttpStatus.OK);
    }

    @ApiOperation(value = "List des rendez-vous")
    @GetMapping("/lire")
    public Iterable<Object[]> getRdv(){
        return rendezVousService.getListRdv();
    }

    @ApiOperation(value = "Suppression rendez-vous")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> supprimer(@PathVariable long id) {
        try {
            if(!rendezVousRepository.existsById(id))
                return new ResponseEntity(new Message("Id n'existe pas"), HttpStatus.NOT_FOUND);
            rendezVousService.delete(id);
            return new ResponseEntity(new Message("Rendez-vous annuler avec succès"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Message("Erreur de creation"), HttpStatus.OK);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@RequestParam(value = "data") String acti,
                                         @PathVariable("id") Long id)
            throws IOException {
        RendezVous rendezVous = null;

        try {
            rendezVous = new JsonMapper().readValue(acti, RendezVous.class);
//            Random e = new Random();
//            e.nextInt(8);
            rendezVousService.modifier(id, rendezVous);
            return new ResponseEntity(new Message("Rendez-vous modifieé avec success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Message("Erreur de modification"), HttpStatus.OK);
        }
    }



}
