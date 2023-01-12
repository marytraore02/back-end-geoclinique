package geoclinique.geoclinique.controller;

import geoclinique.geoclinique.model.Patients;
import geoclinique.geoclinique.repository.PatientRepository;
import geoclinique.geoclinique.service.PatientSevice;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/patient")
@Api(value = "hello", description = "CRUD PATIENT")
public class PatientController {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PatientSevice patientSevice;


}
