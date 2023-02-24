package geoclinique.geoclinique.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;
//    @Lob
//    private  String description;
    private  String titre;
    private Date datenotif;

    public Notification() {

    }

    @OneToOne
    @JoinColumn(name = "id_clinique")
    private Clinique clinique;
}
