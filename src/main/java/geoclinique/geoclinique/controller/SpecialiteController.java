package geoclinique.geoclinique.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import geoclinique.geoclinique.configuration.ImageConfig;
import geoclinique.geoclinique.dto.Message;
import geoclinique.geoclinique.model.Clinics;
import geoclinique.geoclinique.model.Utilisateur;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import geoclinique.geoclinique.model.Specialites;
import geoclinique.geoclinique.repository.SpecialiteRepository;
import geoclinique.geoclinique.service.SpecialiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/specialite")
@Api(value = "hello", description = "CRUD SPECIALITE")
public class SpecialiteController {
    @Autowired
    SpecialiteRepository specialiteRepository;
    @Autowired
    SpecialiteService specialiteService;


    @PreAuthorize("hasRole('ADMIN') or hasRole('CLINIC')")
    @ApiOperation(value = "Creation de specialite")
    @PostMapping("/create/new")
    public ResponseEntity<?> create(@RequestParam(value = "data") String acti,
                                    @RequestParam(value = "file", required = false) MultipartFile file)

            throws IOException {
        Specialites specialites = null;
        try {
            specialites = new JsonMapper().readValue(acti, Specialites.class);

            if(StringUtils.isBlank(specialites.getLibelleSpecialite()))
                return new ResponseEntity(new Message("Le nom de la specialite est obligatoire"), HttpStatus.BAD_REQUEST);

            if(specialiteService.existsByName(specialites.getLibelleSpecialite()))
                return new ResponseEntity(new Message("Ce nom existe déjà"), HttpStatus.BAD_REQUEST);

           // System.out.println("==========Nom specialite===========" + specialites);

            if (file != null) {
                try {
                    specialites.setImageSpecialite(ImageConfig.save("specialite", file, specialites.getLibelleSpecialite()));
                    specialiteService.creer(specialites);
                    return new ResponseEntity(new Message("Specialite créé avec success"), HttpStatus.OK);


                } catch (Exception e) {
                    // TODO: handle exception
                    return new ResponseEntity(new Message("Erreur de creation"), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity(new Message("Photo introuvable"), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(specialites);
            return new ResponseEntity(new Message("Erreur"), HttpStatus.OK);
        }
    }

    @GetMapping("/read")
    public ResponseEntity<List<Specialites>> Afficher(){
        List<Specialites> specialites = specialiteService.read();
        return new ResponseEntity(specialites, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@RequestParam(value = "data") String acti,
                                         @PathVariable("id") Long id,
                                   @RequestParam(value = "file", required = false) MultipartFile file)
            throws IOException {
        Specialites specialites = null;

        try {
            specialites = new JsonMapper().readValue(acti, Specialites.class);
//            Random e = new Random();
//            e.nextInt(8);
            if (file != null) {
                specialites.setImageSpecialite(ImageConfig.save("specialite", file, specialites.getLibelleSpecialite()));
            }
            specialiteService.modifier(id, specialites);
            return new ResponseEntity(new Message("Specialite modifie"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Message("Erreur de modification"), HttpStatus.OK);
        }
    }


    @ApiOperation(value = "Suppression specialite")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> supprimer(@PathVariable long id) {
        try {
            if(!specialiteService.existsById(id))
                return new ResponseEntity(new Message("Id n'existe pas"), HttpStatus.NOT_FOUND);
            specialiteService.delete(id);
            return new ResponseEntity(new Message("Specialite supprimer avec succès"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new Message("Erreur de creation"), HttpStatus.OK);
        }
    }


    @ApiOperation(value = "Affichager une specialite")
    @GetMapping("/get/{id}")
    public ResponseEntity<Specialites> getById(@PathVariable("id") Long id){
        if(!specialiteService.existsById(id))
            return new ResponseEntity(new Message("Specialite n'existe pas"), HttpStatus.NOT_FOUND);
        Specialites specialites = specialiteService.GetOne(id).get();
        return new ResponseEntity(specialites, HttpStatus.OK);
    }



}
