package geoclinique.geoclinique.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "feedback")
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String email;
    @Lob
    private String contenue;
    private Date date;

    public FeedBack(String name, String email, String contenue, Date date) {
        this.name = name;
        this.email = email;
        this.contenue = contenue;
        this.date = date;
    }

    public FeedBack(String name, String email, String contenue) {
        this.name = name;
        this.email = email;
        this.contenue = contenue;
    }
}
