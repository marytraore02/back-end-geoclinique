package geoclinique.geoclinique.service;

import geoclinique.geoclinique.model.Messages;
import geoclinique.geoclinique.repository.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessagesRepository mesageRepository;

    public List<Messages> Afficher(){
        return mesageRepository.findAll();
    }
}
