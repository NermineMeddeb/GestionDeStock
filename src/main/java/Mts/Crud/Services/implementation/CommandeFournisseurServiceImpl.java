package Mts.Crud.Services.implementation;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;


import Mts.Crud.Dto.ArticleDto;
import Mts.Crud.Dto.CommandeFournisseurDto;
import Mts.Crud.Dto.FournisseurDto;
import Mts.Crud.Dto.LigneCommandeFournisseurDto;
import Mts.Crud.Dto.MvtStkDto;
import Mts.Crud.Exceptions.EntityNotFound;
import Mts.Crud.Exceptions.ErrorCodes;
import Mts.Crud.Exceptions.InvalidEntity;
import Mts.Crud.Exceptions.InvalidOperation;
import Mts.Crud.Model.Article;
import Mts.Crud.Model.CommandeFournisseur;
import Mts.Crud.Model.EtatCommande;
import Mts.Crud.Model.Fournisseur;
import Mts.Crud.Model.LigneCommandeFournisseur;
import Mts.Crud.Model.SourceMvtStk;
import Mts.Crud.Model.TypeMvtStk;
import Mts.Crud.Repository.ArticleRepository;
import Mts.Crud.Repository.FournisseurRepository;
import Mts.Crud.Repository.LigneCommandeFournisseurRepository;
import Mts.Crud.Repository.CommandeFournisseurRepository;

import Mts.Crud.Services.MvtStkService;
import Mts.Crud.Services.CommandeFournisseurService;

import Mts.Crud.Validateur.ArticleValidateur;
import Mts.Crud.Validateur.CommandeFournisseurValidateur;


@Service
@Slf4j
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {

  private final CommandeFournisseurRepository commandeFournisseurRepository;
  private final LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;
  private final FournisseurRepository fournisseurRepository;
  private final ArticleRepository articleRepository;
  private final MvtStkService mvtStkService;

  @Autowired
  public CommandeFournisseurServiceImpl(CommandeFournisseurRepository commandeFournisseurRepository,
                                        FournisseurRepository fournisseurRepository,
                                        ArticleRepository articleRepository,
                                        LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository,
                                        MvtStkService mvtStkService) {
    this.commandeFournisseurRepository = commandeFournisseurRepository;
    this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
    this.fournisseurRepository = fournisseurRepository;
    this.articleRepository = articleRepository;
    this.mvtStkService = mvtStkService;
  }

  @Override
  public CommandeFournisseurDto save(CommandeFournisseurDto dto) {
    List<String> errors = CommandeFournisseurValidateur.validate(dto);

    // Validation de la commande fournisseur
    if (!errors.isEmpty()) {
      log.error("Commande fournisseur n'est pas valide");
      throw new InvalidEntity("La commande fournisseur n'est pas valide", ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID, errors);
    }

    // Vérification si la commande est déjà livrée
    if (dto.getId() != null && dto.isCommandeLivree()) {
      throw new InvalidOperation("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
    }

    // Vérification de l'existence du fournisseur
    Optional<Fournisseur> fournisseur = fournisseurRepository.findById(dto.getFournisseur().getId());
    if (fournisseur.isEmpty()) {
      log.warn("Fournisseur avec ID {} n'a ete trouve dans la BDD", dto.getFournisseur().getId());
      throw new EntityNotFound("Aucun fournisseur avec l'ID " + dto.getFournisseur().getId() + " n'a ete trouve dans la BDD",
          ErrorCodes.FOURNISSEUR_NOT_FOUND);
    }

    List<String> articleErrors = new ArrayList<>();

    // Vérification de l'existence des articles
    if (dto.getLigneCommandeFournisseurs() != null) {
      //Si la liste des lignes de commande fournisseur n'est pas nulle, on utilise une boucle forEach pour parcourir chaque ligne de commande (ligCmdFrs).
      dto.getLigneCommandeFournisseurs().forEach(ligCmdFrs -> {//on verif que tous les champs de ligne commande sont pas null pour les sauvgarder
        if (ligCmdFrs.getArticle() != null) {
          Optional<Article> article = articleRepository.findById(ligCmdFrs.getArticle().getId());
          if (article.isEmpty()) {
            articleErrors.add("L'article avec l'ID " + ligCmdFrs.getArticle().getId() + " n'existe pas");
          }
        } else {
          articleErrors.add("Impossible d'enregistrer une commande avec un article NULL");
        }
      });
    }

    // Lancer une exception si des articles sont invalides
    if (!articleErrors.isEmpty()) {
      log.warn("Article non trouvé dans la BDD");
      throw new InvalidEntity("Article n'existe pas dans la BDD", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
    }

    dto.setDateCommande(Instant.now());
    CommandeFournisseur savedCmdFrs = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(dto));

    // Enregistrer les lignes de commande fournisseur
    if (dto.getLigneCommandeFournisseurs() != null) {
      dto.getLigneCommandeFournisseurs().forEach(ligCmdFrs -> {
        LigneCommandeFournisseur ligneCommandeFournisseur = LigneCommandeFournisseurDto.toEntity(ligCmdFrs);
        ligneCommandeFournisseur.setCommandeFournisseur(savedCmdFrs);
        ligneCommandeFournisseur.setIdEntreprise(savedCmdFrs.getIdEntreprise());
        LigneCommandeFournisseur saveLigne = ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
        effectuerEntree(saveLigne);
      });
    }

    return CommandeFournisseurDto.fromEntity(savedCmdFrs);
  }

  @Override
  public CommandeFournisseurDto findById(Integer id) {
    if (id == null) {
      log.error("Commande fournisseur ID is NULL");
      return null;
    }
    return commandeFournisseurRepository.findById(id)
        .map(CommandeFournisseurDto::fromEntity)
        .orElseThrow(() -> new EntityNotFound(
            "Aucune commande fournisseur n'a ete trouve avec l'ID " + id, ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
        ));
  }

  @Override
  public CommandeFournisseurDto findByCode(String code) {
    if (!StringUtils.hasLength(code)) {
      log.error("Commande fournisseur CODE is NULL");
      return null;
    }
    return commandeFournisseurRepository.findCommandeFournisseurByCode(code)
        .map(CommandeFournisseurDto::fromEntity)
        .orElseThrow(() -> new EntityNotFound(
            "Aucune commande fournisseur n'a ete trouve avec le CODE " + code, ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
        ));
  }

  @Override
  public List<CommandeFournisseurDto> findAll() {
    return commandeFournisseurRepository.findAll().stream()
        .map(CommandeFournisseurDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande) {
    return ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(idCommande).stream()
        .map(LigneCommandeFournisseurDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(Integer id) {
    if (id == null) {
      log.error("Commande fournisseur ID is NULL");
      return;
    }
    List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(id);
    if (!ligneCommandeFournisseurs.isEmpty()) {
      throw new InvalidOperation("Impossible de supprimer une commande fournisseur deja utilisee",
          ErrorCodes.COMMANDE_FOURNISSEUR_ALREADY_IN_USE);
    }
    commandeFournisseurRepository.deleteById(id);
  }

  @Override
  public CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
    checkIdCommande(idCommande);
    if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
      log.error("L'etat de la commande fournisseur is NULL");
      throw new InvalidOperation("Impossible de modifier l'etat de la commande avec un etat null",
          ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
    }
    CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
    commandeFournisseur.setEtatCommande(etatCommande);

    CommandeFournisseur savedCommande = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseur));
    if (commandeFournisseur.isCommandeLivree()) {
      updateMvtStk(idCommande);
    }
    return CommandeFournisseurDto.fromEntity(savedCommande);
  }

  @Override
  public CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
    checkIdCommande(idCommande);
    checkIdLigneCommande(idLigneCommande);

    if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
      log.error("L'ID de la ligne commande is NULL");
      throw new InvalidOperation("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO",
          ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
    }

    CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
    Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = ligneCommandeFournisseurRepository.findById(idLigneCommande);
    if (ligneCommandeFournisseurOptional.isEmpty()) {
      throw new EntityNotFound("Aucune ligne commande fournisseur n'a ete trouve avec l'ID " + idLigneCommande,
          ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND);
    }

    LigneCommandeFournisseur ligneCommandeFournisseur = ligneCommandeFournisseurOptional.get();
    ligneCommandeFournisseur.setQuantite(quantite);
    ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);

    return commandeFournisseur;
  }

  @Override
  public CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
    checkIdCommande(idCommande);
    if (idFournisseur == null) {
      log.error("L'ID du fournisseur is NULL");
      throw new InvalidOperation("Impossible de modifier l'etat de la commande avec un ID fournisseur null",
          ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
    }
    CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
    Optional<Fournisseur> fournisseurOptional = fournisseurRepository.findById(idFournisseur);
    if (fournisseurOptional.isEmpty()) {
      throw new EntityNotFound("Aucun fournisseur n'a ete trouve avec l'ID " + idFournisseur,
          ErrorCodes.FOURNISSEUR_NOT_FOUND);
    }
    commandeFournisseur.setFournisseur(FournisseurDto.fromEntity(fournisseurOptional.get()));
    return CommandeFournisseurDto.fromEntity(
        commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseur))
    );
  }

  @Override
  public CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle) {
    checkIdCommande(idCommande);
    checkIdLigneCommande(idLigneCommande);
    if (newIdArticle == null) {
      log.error("L'ID de l'article is NULL");
      throw new InvalidOperation("Impossible de modifier l'etat de la commande avec un ID article null",
          ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
    }

    CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);

    Optional<LigneCommandeFournisseur> ligneCommandeFournisseur = ligneCommandeFournisseurRepository.findById(idLigneCommande);
    if (ligneCommandeFournisseur.isEmpty()) {
      throw new EntityNotFound("Aucune ligne commande fournisseur n'a ete trouve avec l'ID " + idLigneCommande,
          ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND);
    }

    Optional<Article> articleOptional = articleRepository.findById(newIdArticle);
    if (articleOptional.isEmpty()) {
      throw new EntityNotFound("Aucun article n'a ete trouve avec l'ID " + newIdArticle, ErrorCodes.ARTICLE_NOT_FOUND);
    }

    LigneCommandeFournisseur ligneCommandeFournisseurToSaved = ligneCommandeFournisseur.get();
    ligneCommandeFournisseurToSaved.setArticle(articleOptional.get());
    ligneCommandeFournisseurRepository.save(ligneCommandeFournisseurToSaved);

    return commandeFournisseur;
  }

  private void checkIdCommande(Integer idCommande) {
    if (idCommande == null) {
      log.error("Commande fournisseur ID is NULL");
      throw new InvalidOperation("Impossible de modifier l'etat de la commande avec un ID commande null",
          ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
    }
  }

  private void checkIdLigneCommande(Integer idLigneCommande) {
    if (idLigneCommande == null) {
      log.error("Ligne commande ID is NULL");
      throw new InvalidOperation("Impossible de modifier l'etat de la commande avec un ID ligne commande null",
          ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
    }
  }

  private CommandeFournisseurDto checkEtatCommande(Integer idCommande) {
    CommandeFournisseurDto commandeFournisseur = findById(idCommande);
    if (commandeFournisseur.isCommandeLivree()) {
      throw new InvalidOperation("Impossible de modifier l'etat de la commande lorsqu'elle est livree",
          ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
    }
    return commandeFournisseur;
  }

  private void updateMvtStk(Integer idCommande) {
    List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(idCommande);
    ligneCommandeFournisseurs.forEach(ligneCommandeFournisseur -> {
      effectuerEntree(ligneCommandeFournisseur);
    });
  }

  private void effectuerEntree(LigneCommandeFournisseur lig) {

    MvtStkDto mvtStkDto = MvtStkDto.builder()
        .article(ArticleDto.fromEntity(lig.getArticle()))
        .dateMvt(Instant.now())
        .typeMvt(TypeMvtStk.ENTREE)
        .sourceMvt(SourceMvtStk.COMMANDE_FOURNISSEUR)
        .quantite(lig.getQuantite())
        .idEntreprise(lig.getIdEntreprise())
        .build();
    mvtStkService.entreeStock(mvtStkDto);
  }

  @Override
  public CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteArticle'");
  }
}
