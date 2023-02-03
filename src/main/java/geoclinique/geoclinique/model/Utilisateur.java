package geoclinique.geoclinique.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Utilisateur extends User{
    @NotBlank
    @Size(max = 20)
    private String nomEtPrenom;
    @NotBlank
    @Size(max = 20)
    private String contact;
    @NotBlank
    @Size(max = 20)
    private String date;
    @NotBlank
    @Size(max = 20)
    @Column(unique = true)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;
//    private boolean active;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>();

    public Utilisateur(String nomEtPrenom, String contact, String date, String username, String email, String password) {
        this.nomEtPrenom = nomEtPrenom;
        this.contact = contact;
        this.date = date;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //    public Utilisateur(String username, String email, String password) {
////        super(id);
//        this.username = username;
//        this.email = email;
//        this.password = password;
//    }

}
