package geoclinique.geoclinique.payload.request;

import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.*;

public class SignupRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  @Column(unique = true)
  private String nomEtPrenom;
  @NotBlank
  @Size(max = 20)
  private String contact;
  @NotBlank
  @Size(max = 20)
  private String date;
  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private Set<String> role;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

  public String getNomEtPrenom() {
    return nomEtPrenom;
  }

  public void setNomEtPrenom(String nomEtPrenom) {
    this.nomEtPrenom = nomEtPrenom;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<String> getRole() {
    return this.role;
  }

  public void setRole(Set<String> role) {
    this.role = role;
  }
}
