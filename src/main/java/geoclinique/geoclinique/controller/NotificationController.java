package geoclinique.geoclinique.controller;

import geoclinique.geoclinique.model.Medecins;
import geoclinique.geoclinique.model.Notification;
import geoclinique.geoclinique.repository.NotificationRepository;
import geoclinique.geoclinique.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins ={ "http://localhost:4200/", "http://localhost:8100/", "http://localhost:8200/"  }, maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/notification")
@Api(value = "hello", description = "NOTIFICATION")
public class NotificationController {
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    NotificationService notificationService;

    @ApiOperation(value = "Afficher les notifications")
    @GetMapping("/read")
    public ResponseEntity<List<Notification>> Afficher(){
        List<Notification> notifications = notificationService.Afficher();
        return new ResponseEntity(notifications, HttpStatus.OK);
    }

}
