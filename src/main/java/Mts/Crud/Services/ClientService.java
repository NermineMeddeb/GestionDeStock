package Mts.Crud.Services;

import java.util.List;

import Mts.Crud.Dto.ClientDto;

public interface ClientService {
    ClientDto save(ClientDto dto);

  ClientDto findById(Integer id);

  List<ClientDto> findAll();

  void delete(Integer id);
}
