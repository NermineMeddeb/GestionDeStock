package Mts.Crud.Model.Authentification;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;//Représente une autorité accordée à un utilisateur. C'est généralement un rôle ou une permission.
import org.springframework.security.core.userdetails.User;//Classe de Spring Security qui représente un utilisateur. Elle contient les informations de base sur l'utilisateur, comme le nom d'utilisateur, le mot de passe et les autorités.
// ExtendedUser qui étend la classe User de Spring Security. Cette classe est utilisée pour représenter un utilisateur dans le contexte de la gestion de la sécurité et de l'authentification d'une application
public class ExtendedUser extends User {
  @Getter
  @Setter
  private Integer idEntreprise;

  public ExtendedUser(String username, String password,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
  }

  public ExtendedUser(String username, String password, Integer idEntreprise,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.idEntreprise = idEntreprise;
  }
}