package geoclinique.geoclinique.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "rdv")
public class Rdv {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idRdv;
    @NotBlank
    @Size(max = 20)
    private String heureRdv;
    private boolean statusRdv;
}
