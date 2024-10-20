package Mts.Crud.Services.implementation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

import Mts.Crud.Dto.ArticleDto;
import Mts.Crud.Dto.ClientDto;
import Mts.Crud.Dto.CommandeClientDto;
import Mts.Crud.Dto.LigneCommandeClientDto;
import Mts.Crud.Dto.MvtStkDto;
import Mts.Crud.Exceptions.EntityNotFound;
import Mts.Crud.Exceptions.ErrorCodes;
import Mts.Crud.Exceptions.InvalidEntity;
import Mts.Crud.Exceptions.InvalidOperation;
import Mts.Crud.Model.Article;
import Mts.Crud.Model.Client;
import Mts.Crud.Model.CommandeClient;
import Mts.Crud.Model.EtatCommande;
import Mts.Crud.Model.LigneCommandeClient;
import Mts.Crud.Model.SourceMvtStk;
import Mts.Crud.Model.TypeMvtStk;
import Mts.Crud.Repository.ArticleRepository;
import Mts.Crud.Repository.ClientRepository;
import Mts.Crud.Repository.CommandeClientRepository;
import Mts.Crud.Repository.LigneCommandeClientRepository;
import Mts.Crud.Services.CommandeClientService;
import Mts.Crud.Services.MvtStkService;
import Mts.Crud.Validateur.ArticleValidateur;
import Mts.Crud.Validateur.CommandeClientValidateur;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommandeClientServiceImpl implements CommandeClientService {

  private CommandeClientRepository commandeClientRepository;
  private LigneCommandeClientRepository ligneCommandeClientRepository;
  private ClientRepository clientRepository;
  private ArticleRepository articleRepository;
  private MvtStkService mvtStkService;

  @Autowired
  public CommandeClientServiceImpl(CommandeClientRepository commandeClientRepository,
      ClientRepository clientRepository, ArticleRepository articleRepository, LigneCommandeClientRepository ligneCommandeClientRepository,
      MvtStkService mvtStkService) {
    this.commandeClientRepository = commandeClientRepository;
    this.ligneCommandeClientRepository = ligneCommandeClientRepository;
    this.clientRepository = clientRepository;
    this.articleRepository = articleRepository;
    this.mvtStkService = mvtStkService;
  }

  @Override
  public CommandeClientDto save(CommandeClientDto dto) {
    // Valider le DTO de la commande client
    List<String> errors = CommandeClientValidateur.validate(dto);
    if (!errors.isEmpty()) {
      log.error("Commande client n'est pas valide");
      throw new InvalidEntity("La commande client n'est pas valide", ErrorCodes.COMMANDE_CLIENT_NOT_VALID, errors);
    }

    // Vérifier si la commande est livrée (impossible de modifier une commande livrée)
    if (dto.getId() != null && dto.isCommandeLivree()) {
      throw new InvalidOperation("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }

    // Vérifier si le client existe dans la base de données
    Optional<Client> client = clientRepository.findById(dto.getClient().getId());
    if (client.isEmpty()) {
      log.warn("Client with ID {} was not found in the DB", dto.getClient().getId());
      throw new EntityNotFound("Aucun client avec l'ID" + dto.getClient().getId() + " n'a ete trouve dans la BDD", ErrorCodes.CLIENT_NOT_FOUND);
    }

    List<String> articleErrors = new ArrayList<>();
    // Vérifier si les articles de la commande existent dans la base de données
    if (dto.getLigneCommandeClients() != null) {
      dto.getLigneCommandeClients().forEach(ligCmdClt -> {
        if (ligCmdClt.getArticle() != null) {
          Optional<Article> article = articleRepository.findById(ligCmdClt.getArticle().getId());
          if (article.isEmpty()) {
            articleErrors.add("L'article avec l'ID " + ligCmdClt.getArticle().getId() + " n'existe pas");
          }
        } else {
          articleErrors.add("Impossible d'enregistrer une commande avec un article NULL");
        }
      });
    }

    if (!articleErrors.isEmpty()) {
      log.warn("Articles non trouvés dans la BDD");
      throw new InvalidEntity("Article n'existe pas dans la BDD", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
    }

    // Définir la date de commande
    dto.setDateCommande(LocalDate.now());
    // Sauvegarder la commande client
    CommandeClient savedCmdClt = commandeClientRepository.save(CommandeClientDto.toEntity(dto));

    // Sauvegarder les lignes de commande client
    if (dto.getLigneCommandeClients() != null) {
      dto.getLigneCommandeClients().forEach(ligCmdClt -> {
        LigneCommandeClient ligneCommandeClient = LigneCommandeClientDto.toEntity(ligCmdClt);
        ligneCommandeClient.setCommandeClient(savedCmdClt);
        ligneCommandeClient.setIdEntreprise(dto.getIdEntreprise());
        LigneCommandeClient savedLigneCmd = ligneCommandeClientRepository.save(ligneCommandeClient);

        effectuerSortie(savedLigneCmd);
      });
    }

    return CommandeClientDto.fromEntity(savedCmdClt);
  }

  @Override
  public CommandeClientDto findById(Integer id) {
    if (id == null) {
      log.error("Commande client ID is NULL");
      return null;
    }
    return commandeClientRepository.findById(id)
        .map(CommandeClientDto::fromEntity)
        .orElseThrow(() -> new EntityNotFound(
            "Aucune commande client n'a ete trouve avec l'ID " + id, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
        ));
  }

  @Override
  public CommandeClientDto findByCode(String code) {
    if (!StringUtils.hasLength(code)) {
      log.error("Commande client CODE is NULL");
      return null;
    }
    return commandeClientRepository.findCommandeClientByCode(code)
        .map(CommandeClientDto::fromEntity)
        .orElseThrow(() -> new EntityNotFound(
            "Aucune commande client n'a ete trouve avec le CODE " + code, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
        ));
  }

  @Override
  public List<CommandeClientDto> findAll() {
    return commandeClientRepository.findAll().stream()
        .map(CommandeClientDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(Integer id) {
    if (id == null) {
      log.error("Commande client ID is NULL");
      return;
    }
    List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(id);
    if (!ligneCommandeClients.isEmpty()) {
      throw new InvalidOperation("Impossible de supprimer une commande client déjà utilisée", ErrorCodes.COMMANDE_CLIENT_ALREADY_IN_USE);
    }
    commandeClientRepository.deleteById(id);
  }

  @Override
  public List<LigneCommandeClientDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande) {
    return ligneCommandeClientRepository.findAllByCommandeClientId(idCommande).stream()
        .map(LigneCommandeClientDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public CommandeClientDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
    checkIdCommande(idCommande);
    if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
      log.error("L'etat de la commande client is NULL");
      throw new InvalidOperation("Impossible de modifier l'etat de la commande avec un etat null", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }
    CommandeClientDto commandeClient = checkEtatCommande(idCommande);
    commandeClient.setEtatCommande(etatCommande);

    CommandeClient savedCmdClt = commandeClientRepository.save(CommandeClientDto.toEntity(commandeClient));
    if (commandeClient.isCommandeLivree()) {
      updateMvtStk(idCommande);
    }

    return CommandeClientDto.fromEntity(savedCmdClt);
  }

  @Override
  public CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
    checkIdCommande(idCommande);
    checkIdLigneCommande(idLigneCommande);

    if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
      log.error("La quantité de la ligne de commande est NULL ou ZERO");
      throw new InvalidOperation("Impossible de modifier l'etat de la commande avec une quantité null ou ZERO", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }

    CommandeClientDto commandeClient = checkEtatCommande(idCommande);
    Optional<LigneCommandeClient> ligneCommandeClientOptional = findLigneCommandeClient(idLigneCommande);

    LigneCommandeClient ligneCommandeClient = ligneCommandeClientOptional.get();
    ligneCommandeClient.setQuantite(quantite);
    ligneCommandeClientRepository.save(ligneCommandeClient);

    return commandeClient;
  }

  @Override
  public CommandeClientDto updateClient(Integer idCommande, Integer idClient) {
    checkIdCommande(idCommande);
    if (idClient == null) {
      log.error("L'ID du client est NULL");
      throw new InvalidOperation("Impossible de modifier l'etat de la commande avec un ID client null", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }
    CommandeClientDto commandeClient = checkEtatCommande(idCommande);
    Optional<Client> clientOptional = clientRepository.findById(idClient);
    if (clientOptional.isEmpty()) {
      throw new EntityNotFound("Aucun client n'a été trouvé avec l'ID " + idClient, ErrorCodes.CLIENT_NOT_FOUND);
    }
    commandeClient.setClient(ClientDto.fromEntity(clientOptional.get()));

    return CommandeClientDto.fromEntity(commandeClientRepository.save(CommandeClientDto.toEntity(commandeClient)));
  }

  @Override
  public CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
    checkIdCommande(idCommande);
    checkIdLigneCommande(idLigneCommande);
    checkIdArticle(idArticle, "nouvel");

    CommandeClientDto commandeClient = checkEtatCommande(idCommande);

    Optional<LigneCommandeClient> ligneCommandeClient = findLigneCommandeClient(idLigneCommande);

    Optional<Article> articleOptional = articleRepository.findById(idArticle);
    if (articleOptional.isEmpty()) {
      throw new EntityNotFound("Aucun article n'a été trouvé avec l'ID " + idArticle, ErrorCodes.ARTICLE_NOT_FOUND);
    }

    List<String> errors = ArticleValidateur.validate(ArticleDto.fromEntity(articleOptional.get()));
    if (!errors.isEmpty()) {
      throw new InvalidEntity("Article invalide", ErrorCodes.ARTICLE_NOT_VALID, errors);
    }

    LigneCommandeClient ligneCommandeClientToSaved = ligneCommandeClient.get();
    ligneCommandeClientToSaved.setArticle(articleOptional.get());
    ligneCommandeClientRepository.save(ligneCommandeClientToSaved);

    return commandeClient;
  }

  @Override
  public CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
    checkIdCommande(idCommande);
    checkIdLigneCommande(idLigneCommande);

    CommandeClientDto commandeClient = checkEtatCommande(idCommande);
    findLigneCommandeClient(idLigneCommande);
    ligneCommandeClientRepository.deleteById(idLigneCommande);

    return commandeClient;
  }

  // Vérifie l'état de la commande (si elle est livrée ou non)
  private CommandeClientDto checkEtatCommande(Integer idCommande) {
    CommandeClientDto commandeClient = findById(idCommande);
    if (commandeClient.isCommandeLivree()) {
      throw new InvalidOperation("Impossible de modifier la commande lorsqu'elle est livrée", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }
    return commandeClient;
  }

  // Trouver une ligne de commande client par ID
  private Optional<LigneCommandeClient> findLigneCommandeClient(Integer idLigneCommande) {
    Optional<LigneCommandeClient> ligneCommandeClientOptional = ligneCommandeClientRepository.findById(idLigneCommande);
    if (ligneCommandeClientOptional.isEmpty()) {
      throw new EntityNotFound("Aucune ligne commande client n'a été trouvée avec l'ID " + idLigneCommande, ErrorCodes.COMMANDE_CLIENT_NOT_FOUND);
    }
    return ligneCommandeClientOptional;
  }

  // Vérifie si l'ID de la commande n'est pas NULL
  private void checkIdCommande(Integer idCommande) {
    if (idCommande == null) {
      log.error("Commande client ID is NULL");
      throw new InvalidOperation("Impossible de modifier l'état de la commande avec un ID null", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }
  }

  // Vérifie si l'ID de la ligne de commande n'est pas NULL
  private void checkIdLigneCommande(Integer idLigneCommande) {
    if (idLigneCommande == null) {
      log.error("L'ID de la ligne de commande est NULL");
      throw new InvalidOperation("Impossible de modifier l'état de la commande avec une ligne de commande null", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }
  }

  // Vérifie si l'ID de l'article n'est pas NULL
  private void checkIdArticle(Integer idArticle, String msg) {
    if (idArticle == null) {
      log.error("L'ID de " + msg + " article est NULL");
      throw new InvalidOperation("Impossible de modifier l'état de la commande avec un " + msg + " ID article null", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
    }
  }

  // Met à jour le mouvement de stock pour une commande livrée
  private void updateMvtStk(Integer idCommande) {
    List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(idCommande);
    ligneCommandeClients.forEach(this::effectuerSortie);
  }

  // Effectue une sortie de stock pour une ligne de commande client
  private void effectuerSortie(LigneCommandeClient lig) {
    MvtStkDto mvtStkDto = MvtStkDto.builder()
        .article(ArticleDto.fromEntity(lig.getArticle()))
        .dateMvt(LocalDate.now())
        .typeMvt(TypeMvtStk.SORTIE)
        .sourceMvt(SourceMvtStk.COMMANDE_CLIENT)
        .quantite(lig.getQuantite())
        .idEntreprise(lig.getIdEntreprise())
        .build();
    mvtStkService.sortieStock(mvtStkDto);
  }
}
