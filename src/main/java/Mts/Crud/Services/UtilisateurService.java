package Mts.Crud.Services;

import Mts.Crud.Dto.ChangerMotDePasseUtilisateurDto;
import Mts.Crud.Dto.UtilisateurDto;
import java.util.List;

public interface UtilisateurService {
    UtilisateurDto save(UtilisateurDto dto);
    UtilisateurDto findById(Integer id);
    List<UtilisateurDto> findAll();
    void delete(Integer id);
    UtilisateurDto findByEmail(String email);
    UtilisateurDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto);
}
