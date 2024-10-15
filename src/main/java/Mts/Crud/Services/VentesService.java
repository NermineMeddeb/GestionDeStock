package Mts.Crud.Services;

import java.util.List;

import Mts.Crud.Dto.VentesDto;

public interface VentesService {

     VentesDto save(VentesDto dto);

  VentesDto findById(Integer id);

  VentesDto findByCode(String code);

  List<VentesDto> findAll();

  void delete(Integer id);
}  
    

