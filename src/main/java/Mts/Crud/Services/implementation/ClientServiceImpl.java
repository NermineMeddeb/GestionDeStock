package Mts.Crud.Services.implementation;

import org.springframework.stereotype.Service;

import Mts.Crud.Dto.ClientDto;
import Mts.Crud.Exceptions.EntityNotFound;
import Mts.Crud.Exceptions.ErrorCodes;
import Mts.Crud.Exceptions.InvalidEntity;
import Mts.Crud.Exceptions.InvalidOperation;
import Mts.Crud.Model.CommandeClient;
import Mts.Crud.Repository.ClientRepository;
import Mts.Crud.Repository.CommandeClientRepository;
import Mts.Crud.Services.ClientService;
import Mts.Crud.Validateur.ClientValidateur;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

  private ClientRepository clientRepository;
  private CommandeClientRepository commandeClientRepository;

  @Autowired
  public ClientServiceImpl(ClientRepository clientRepository, CommandeClientRepository commandeClientRepository) {
    this.clientRepository = clientRepository;
    this.commandeClientRepository = commandeClientRepository;
  }

  @Override
  public ClientDto save(ClientDto dto) {
    List<String> errors = ClientValidateur.validate(dto);
    if (!errors.isEmpty()) {
      log.error("Client is not valid {}", dto);
      throw new InvalidEntity("Le client n'est pas valide", ErrorCodes.CLIENT_NOT_VALID, errors);
    }

    return ClientDto.fromEntity(
        clientRepository.save(
            ClientDto.toEntity(dto)
        )
    );
  }

  @Override
  public ClientDto findById(Integer id) {
    if (id == null) {
      log.error("Client ID is null");
      return null;
    }
    return clientRepository.findById(id)
        .map(ClientDto::fromEntity)
        .orElseThrow(() -> new EntityNotFound(
            "Aucun Client avec l'ID = " + id + " n' ete trouve dans la BDD",
            ErrorCodes.CLIENT_NOT_FOUND)
        );
  }

  @Override
  public List<ClientDto> findAll() {
    return clientRepository.findAll().stream()
        .map(ClientDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(Integer id) {
    if (id == null) {
      log.error("Client ID is null");
      return;
    }
    List<CommandeClient> commandeClients = commandeClientRepository.findAllByClientId(id);
    if (!commandeClients.isEmpty()) {
      throw new InvalidOperation("Impossible de supprimer un client qui a deja des commande clients",
          ErrorCodes.CLIENT_ALREADY_IN_USE);
    }
    clientRepository.deleteById(id);
  }
}