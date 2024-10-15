package Mts.Crud.Services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Mts.Crud.Dto.FournisseurDto;
import Mts.Crud.Exceptions.EntityNotFound;
import Mts.Crud.Exceptions.ErrorCodes;
import Mts.Crud.Exceptions.InvalidEntity;
import Mts.Crud.Exceptions.InvalidOperation;
import Mts.Crud.Model.CommandeClient;
import Mts.Crud.Repository.CommandeFournisseurRepository;
import Mts.Crud.Repository.FournisseurRepository;
import Mts.Crud.Services.FournisseurService;
import Mts.Crud.Validateur.FournisseurValidateur;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FournisseurServiceImpl implements FournisseurService {

  private final FournisseurRepository fournisseurRepository;
  private final CommandeFournisseurRepository commandeFournisseurRepository;

  @Autowired
  public FournisseurServiceImpl(FournisseurRepository fournisseurRepository,
      CommandeFournisseurRepository commandeFournisseurRepository) {
    this.fournisseurRepository = fournisseurRepository;
    this.commandeFournisseurRepository = commandeFournisseurRepository;
  }

  @Override
  public FournisseurDto save(FournisseurDto dto) {
    List<String> errors = FournisseurValidateur.validate(dto);
    if (!errors.isEmpty()) {
      log.error("Fournisseur is not valid {}", dto);
      throw new InvalidEntity("Le fournisseur n'est pas valide", ErrorCodes.FOURNISSEUR_NOT_VALID, errors);
    }

    return FournisseurDto.fromEntity(
        fournisseurRepository.save(
            FournisseurDto.toEntity(dto)
        )
    );
  }

  @Override
  public FournisseurDto findById(Integer id) {
    if (id == null) {
      log.error("Fournisseur ID is null");
      return null;
    }
    return fournisseurRepository.findById(id)
        .map(FournisseurDto::fromEntity)
        .orElseThrow(() -> new EntityNotFound(
            "Aucun fournisseur avec l'ID = " + id + " n'a été trouvé dans la BDD",
            ErrorCodes.FOURNISSEUR_NOT_FOUND)
        );
  }

  @Override
  public List<FournisseurDto> findAll() {
    return fournisseurRepository.findAll().stream()
        .map(FournisseurDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(Integer id) {
    if (id == null) {
      log.error("Fournisseur ID is null");
      return;
    }
    List<CommandeClient> commandeFournisseur = commandeFournisseurRepository.findAllByFournisseurId(id);
    if (!commandeFournisseur.isEmpty()) {
      throw new InvalidOperation("Impossible de supprimer un fournisseur qui a déjà des commandes",
          ErrorCodes.FOURNISSEUR_ALREADY_IN_USE);
    }
    fournisseurRepository.deleteById(id);
  }
}
