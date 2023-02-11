package geoclinique.geoclinique.payload.response;

import java.util.List;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String nomEtPrenom;
  private String contact;
  private String date;
  private String image;

  private String username;
  private String email;
  private List<String> roles;

//  public JwtResponse(String accessToken, Long id, String nomEtPrenom, String contact, String date, String username, String email, List<String> roles) {
//    this.token = accessToken;
//    this.id = id;
//    this.nomEtPrenom = nomEtPrenom;
//    this.contact = contact;
//    this.date = date;
//    this.username = username;
//    this.email = email;
//    this.roles = roles;
//  }

  public JwtResponse(String accessToken, Long id, String nomEtPrenom, String contact, String date, String image, String username, String email, List<String> roles) {
    this.token = accessToken;
    this.id = id;
    this.nomEtPrenom = nomEtPrenom;
    this.contact = contact;
    this.date = date;
    this.image = image;
    this.username = username;
    this.email = email;
    this.roles = roles;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNomEtPrenom() {
    return nomEtPrenom;
  }

  public void setNomEtPrenom(String nomEtPrenom) {
    this.nomEtPrenom = nomEtPrenom;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getRoles() {
    return roles;
  }
}
