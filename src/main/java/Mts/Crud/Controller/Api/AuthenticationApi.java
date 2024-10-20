package Mts.Crud.Controller.Api;


import static Mts.Crud.Utils.Constants.AUTHENTICATION_ENDPOINT;

import Mts.Crud.Dto.Authentification.AuthenticationRequest;
import Mts.Crud.Dto.Authentification.AuthenticationResponse;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//@Api("authentication")
public interface AuthenticationApi {

  @PostMapping(AUTHENTICATION_ENDPOINT + "/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request);

}
