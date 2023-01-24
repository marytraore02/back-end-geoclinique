package geoclinique.geoclinique.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "admin",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Admin extends Utilisateur{

    @NotBlank
    @Size(max = 20)
    private String nom;
    @NotBlank
    @Size(max = 20)
    private String prenom;
    @NotBlank
    @Size(max = 20)
    private String contact;

    public Admin(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 50) @Email String email, @NotBlank @Size(max = 120) String password, String nom, String prenom, String contact) {
        super(username, email, password);
        this.nom = nom;
        this.prenom = prenom;
        this.contact = contact;
    }
}
