package Mts.Crud.Controller;

import Mts.Crud.Controller.Api.AuthenticationApi;
import Mts.Crud.Dto.Authentification.AuthenticationRequest;
import Mts.Crud.Dto.Authentification.AuthenticationResponse;
import Mts.Crud.Model.Authentification.*;
import Mts.Crud.Utils.*;
import Mts.Crud.Services.Authentification.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour l'authentification des utilisateurs.
 */
@RestController
public class AuthenticationController implements AuthenticationApi {

  @Autowired
  private AuthenticationManager authenticationManager; // Gère l'authentification des utilisateurs

  @Autowired
  private ApplicationUserDetailsService userDetailsService; // Service pour charger les détails de l'utilisateur

  @Autowired
  private JwtUtil jwtUtil; // Utilitaire pour la gestion des tokens JWT

  /**
   * Authentifie l'utilisateur et génère un jeton d'accès JWT.
   *
   * @param request Requête d'authentification contenant le login et le mot de passe
   * @return Réponse contenant le jeton d'accès
   */
  @Override
  public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
    // Authentifie l'utilisateur avec les informations fournies
  /*   authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getLogin(), // Nom d'utilisateur
            request.getPassword() // Mot de passe
        )
    );*/
    // Charge les détails de l'utilisateur
    final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getLogin());

    // Génère un token JWT pour l'utilisateur authentifié
    final String jwt = jwtUtil.generateToken((ExtendedUser) userDetails);

    // Retourne le token dans la réponse
    return ResponseEntity.ok(AuthenticationResponse.builder().accessToken(jwt).build());
  }
}
