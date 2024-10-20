package Mts.Crud.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import Mts.Crud.Controller.Api.ClientApi;
import Mts.Crud.Dto.ClientDto;
import Mts.Crud.Services.ClientService;
@CrossOrigin(origins = "http://localhost:4200") // Permet l'accès à cette ressource

public class ClientController implements ClientApi  {
      @GetMapping("/gestiondestock/v1/clients/all")

    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
      this.clientService = clientService;
    }
  
    @Override
    public ClientDto save(ClientDto dto) {
      return clientService.save(dto);
    }
  
    @Override
    public ClientDto findById(Integer id) {
      return clientService.findById(id);
    }
  
    @Override
    public List<ClientDto> findAll() {
      return clientService.findAll();
    }
  
    @Override
    public void delete(Integer id) {
      clientService.delete(id);
    }
}
