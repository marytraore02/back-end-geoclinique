package geoclinique.geoclinique.service;

import geoclinique.geoclinique.model.RendezVous;
import geoclinique.geoclinique.repository.RendezVousRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RendezVousService {
    @Autowired
    RendezVousRepository rendezVousRepository;

    public List<RendezVous> read(){
        return rendezVousRepository.findAll();
    }

}
