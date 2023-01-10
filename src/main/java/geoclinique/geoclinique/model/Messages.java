package geoclinique.geoclinique.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idMessage;
//    private Long idExpMessage;
//    private Long idDestMessage;

    @Lob
    private String contenue;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "clinics")
    Clinics clinics;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "patients")
    Patients patients;
}
