package Mts.Crud.Services;

import java.util.List;

import Mts.Crud.Dto.FournisseurDto;

public interface FournisseurService {
     FournisseurDto save(FournisseurDto dto);

  FournisseurDto findById(Integer id);

  List<FournisseurDto> findAll();

  void delete(Integer id);

}

