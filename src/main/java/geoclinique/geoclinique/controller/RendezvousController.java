package geoclinique.geoclinique.controller;

import geoclinique.geoclinique.model.Medecins;
import geoclinique.geoclinique.model.RendezVous;
import geoclinique.geoclinique.repository.RendezVousRepository;
import geoclinique.geoclinique.service.RendezVousService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "Afficher la liste des rendez-vous")
    @GetMapping("/read")
    public List<RendezVous> Read(){
        List<RendezVous> rdv = rendezVousService.read();
        return rdv;
    }
}
