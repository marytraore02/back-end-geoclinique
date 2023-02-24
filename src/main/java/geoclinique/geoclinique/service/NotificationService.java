package geoclinique.geoclinique.service;

import geoclinique.geoclinique.model.Notification;
import geoclinique.geoclinique.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    public Notification creer(Notification notification){
        return notificationRepository.save(notification);
    }
    public List<Notification> Afficher(){
        return notificationRepository.findAll();
    }
}
