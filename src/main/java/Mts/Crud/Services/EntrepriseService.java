package Mts.Crud.Services;

import java.util.List;

import Mts.Crud.Dto.EntrepriseDto;

public interface EntrepriseService {

  EntrepriseDto save(EntrepriseDto dto);

  EntrepriseDto findById(Integer id);

  List<EntrepriseDto> findAll();

  void delete(Integer id);
}
