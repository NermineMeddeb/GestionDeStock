package Mts.Crud.Services.implementation;



import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Mts.Crud.Dto.EntrepriseDto;
import Mts.Crud.Dto.RolesDto;
import Mts.Crud.Dto.UtilisateurDto;
import Mts.Crud.Exceptions.EntityNotFound;
import Mts.Crud.Exceptions.ErrorCodes;
import Mts.Crud.Exceptions.InvalidEntity;
import Mts.Crud.Repository.EntrepriseRepository;
import Mts.Crud.Repository.RoleRepository;
import Mts.Crud.Services.EntrepriseService;
import Mts.Crud.Services.UtilisateurService;
import Mts.Crud.Validateur.EntrepriseValidateur;
import jakarta.transaction.Transactional;

@Transactional(rollbackOn = Exception.class)
@Service
@Slf4j
public class EntrepriseServiceImpl implements EntrepriseService {

  private EntrepriseRepository entrepriseRepository;
  //private UtilisateurService utilisateurService;
  private RoleRepository rolesRepository;

  @Autowired
  public EntrepriseServiceImpl(EntrepriseRepository entrepriseRepository,
  // UtilisateurService utilisateurService,
  RoleRepository rolesRepository) {
    this.entrepriseRepository = entrepriseRepository;
   // this.utilisateurService = utilisateurService;
    this.rolesRepository = rolesRepository;
  }

  @Override
  public EntrepriseDto save(EntrepriseDto dto) {
    List<String> errors = EntrepriseValidateur.validate(dto);
    if (!errors.isEmpty()) {
      log.error("Entreprise is not valid {}", dto);
      throw new InvalidEntity("L'entreprise n'est pas valide", ErrorCodes.ENTREPRISE_NOT_VALID, errors);
    }
    EntrepriseDto savedEntreprise = EntrepriseDto.fromEntity(
        entrepriseRepository.save(EntrepriseDto.toEntity(dto))
    );

    UtilisateurDto utilisateur = fromEntreprise(savedEntreprise);

    /*UtilisateurDto savedUser = utilisateurService.save(utilisateur);

    RolesDto rolesDto = RolesDto.builder()
        .roleName("ADMIN")
        .utilisateur(savedUser)
        .build();

    rolesRepository.save(RolesDto.toEntity(rolesDto));*/

    return  savedEntreprise;
  }

  private UtilisateurDto fromEntreprise(EntrepriseDto dto) {
    return UtilisateurDto.builder()
        .adresse(dto.getAdresse())
        .nom(dto.getNom())
        .prenom(dto.getCodeFiscal())
        .email(dto.getEmail())
        .moteDePasse(generateRandomPassword())
        .entreprise(dto)
        .dateDeNaissance(LocalDate.now())
        .photo(dto.getPhoto())
        .build();
  }

  private String generateRandomPassword() {
    return "tiaymdp";
  }

  @Override
  public EntrepriseDto findById(Integer id) {
    if (id == null) {
      log.error("Entreprise ID is null");
      return null;
    }
    return entrepriseRepository.findById(id)
        .map(EntrepriseDto::fromEntity)
        .orElseThrow(() -> new EntityNotFound(
            "Aucune entreprise avec l'ID = " + id + " n' ete trouve dans la BDD",
            ErrorCodes.ENTREPRISE_NOT_FOUND)
        );
  }

  @Override
  public List<EntrepriseDto> findAll() {
    return entrepriseRepository.findAll().stream()
        .map(EntrepriseDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(Integer id) {
    if (id == null) {
      log.error("Entreprise ID is null");
      return;
    }
    entrepriseRepository.deleteById(id);
  }
}
