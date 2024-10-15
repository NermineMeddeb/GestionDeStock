package Mts.Crud.Controller.Api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Mts.Crud.Dto.ClientDto;
import static Mts.Crud.Utils.Constants.APP_ROOT;

import java.util.List;

@RestController
@RequestMapping(APP_ROOT + "/clients")
public interface ClientApi {

  @PostMapping(value = APP_ROOT + "/clients/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ClientDto save(@RequestBody ClientDto dto);

  @GetMapping(value = APP_ROOT + "/clients/{idClient}", produces = MediaType.APPLICATION_JSON_VALUE)
  ClientDto findById(@PathVariable("idClient") Integer id);

  @GetMapping(value = APP_ROOT + "/clients/all", produces = MediaType.APPLICATION_JSON_VALUE)
  List<ClientDto> findAll();

  @DeleteMapping(value = APP_ROOT + "/clients/delete/{idClient}")
  void delete(@PathVariable("idClient") Integer id);

    
}
