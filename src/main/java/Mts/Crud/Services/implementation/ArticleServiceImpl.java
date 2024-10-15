package Mts.Crud.Services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.cj.util.StringUtils;

import Mts.Crud.Dto.ArticleDto;
import Mts.Crud.Dto.LigneCommandeClientDto;
import Mts.Crud.Dto.LigneCommandeFournisseurDto;
import Mts.Crud.Dto.LigneVenteDto;
import Mts.Crud.Exceptions.ErrorCodes;
import Mts.Crud.Exceptions.InvalidEntity;
import Mts.Crud.Exceptions.EntityNotFound;
import Mts.Crud.Model.Article;
import Mts.Crud.Model.LigneCommandeClient;
import Mts.Crud.Model.LigneCommandeFournisseur;
import Mts.Crud.Model.LigneVente;
import Mts.Crud.Repository.ArticleRepository;
import Mts.Crud.Repository.LigneCommandeClientRepository;
import Mts.Crud.Repository.LigneCommandeFournisseurRepository;
import Mts.Crud.Repository.LigneVenteRepository;
import Mts.Crud.Services.ArticleService;
import Mts.Crud.Validateur.ArticleValidateur;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    // Déclaration des repositories utilisés par le service
    private ArticleRepository articleRepository;
    private LigneVenteRepository ligneVenteRepository;
    private LigneCommandeFournisseurRepository commandeFournisseurRepository;
    private LigneCommandeClientRepository commandeClientRepository;

    // Injection de dépendances par constructeur
    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, 
                              LigneVenteRepository ligneVenteRepository, 
                              LigneCommandeFournisseurRepository commandeFournisseurRepository, 
                              LigneCommandeClientRepository commandeClientRepository) {
        this.articleRepository = articleRepository;
        this.ligneVenteRepository = ligneVenteRepository;
        this.commandeFournisseurRepository = commandeFournisseurRepository;
        this.commandeClientRepository = commandeClientRepository;
    }

    @Override
    public ArticleDto save(ArticleDto dto) {
        // Validation de l'article
        List<String> errors = ArticleValidateur.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Cet article n'est pas valide {}", dto);
            throw new InvalidEntity("Cet article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID);
        }
        // Conversion du DTO en entité et sauvegarde dans la base de données
        return ArticleDto.fromEntity(
                articleRepository.save(
                        ArticleDto.toEntity(dto)
                )
        );
    }

    @Override
    public ArticleDto findById(Integer id) {
        // Vérification si l'ID est null
        if (id == null) {
            log.error("L'id de l'article est null");
            return null;
        }
        // Recherche de l'article par ID
        Optional<Article> article = articleRepository.findById(id);
        // Conversion de l'entité en DTO ou lancement d'une exception si non trouvé
        return article.map(ArticleDto::fromEntity)
                      .orElseThrow(() -> new EntityNotFound(
                          "Aucun article avec l'ID " + id + " n'a été trouvé dans la base de données", 
                          ErrorCodes.ARTICLE_NOT_FOUND));
    }

    @Override
    public ArticleDto findByCodeArticle(String codeArticle) {
        // Vérification si le code article est null ou vide
        if (StringUtils.isNullOrEmpty(codeArticle)) {
            log.error("Le code de l'article est null ou vide");
            return null;
        }
        // Recherche de l'article par code
        Optional<Article> article = articleRepository.findByCodeArticle(codeArticle);
        // Conversion de l'entité en DTO ou lancement d'une exception si non trouvé
        return article.map(ArticleDto::fromEntity)
                      .orElseThrow(() -> new EntityNotFound(
                          "Aucun article avec le code " + codeArticle + " n'a été trouvé dans la base de données", 
                          ErrorCodes.ARTICLE_NOT_FOUND));
    }

    @Override
public List<ArticleDto> findAll() {
    // Récupération de tous les articles depuis le dépôt
    // `findAll()` retourne une liste d'objets Article
    return articleRepository.findAll().stream()
        // Conversion de chaque Article en ArticleDto à l'aide de la méthode fromEntity
        .map(ArticleDto::fromEntity)
        // Collecte des ArticleDto dans une liste
        .collect(Collectors.toList());
}


    @Override
    public List<LigneVenteDto> findHistoriqueVentes(Integer idArticle) {
        // Récupération de l'historique des ventes d'un article et conversion en DTO
        return ligneVenteRepository.findAllByArticleId(idArticle).stream()
        .map(LigneVenteDto::fromEntity)
        .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeClientDto> findHistoriqueCommandeClient(Integer idArticle) {
        // Récupération de l'historique des commandes clients pour un article et conversion en DTO
        return commandeClientRepository.findAllByArticleId(idArticle).stream()
            .map(LigneCommandeClientDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle) {
        // Récupération de l'historique des commandes fournisseurs pour un article et conversion en DTO
        return commandeFournisseurRepository.findAllByArticleId(idArticle).stream()
            .map(x->LigneCommandeFournisseurDto.fromEntity(null))
            .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDto> findAllArticleByIdCategory(Integer idCategory) {
        // Récupération de tous les articles par catégorie et conversion en DTO
        return articleRepository.findAllByCategoryId(idCategory).stream()
            .map(ArticleDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        // Vérification si l'ID est null
        if (id == null) {
            log.error("L'ID de l'article est null");
            return;
        }
        // Vérification des commandes clients associées à l'article
        List<LigneCommandeClient> ligneCommandeClients = commandeClientRepository.findAllByArticleId(id);
        if (!ligneCommandeClients.isEmpty()) {
            throw new InvalidEntity("Impossible de supprimer un article déjà utilisé dans des commandes client", ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }
        // Vérification des commandes fournisseurs associées à l'article
        List<LigneCommandeFournisseur> ligneCommandeFournisseurs = commandeFournisseurRepository.findAllByArticleId(id);
        if (!ligneCommandeFournisseurs.isEmpty()) {
            throw new InvalidEntity("Impossible de supprimer un article déjà utilisé dans des commandes fournisseur", ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }
        // Vérification des ventes associées à l'article
        List<LigneVente> ligneVentes = ligneVenteRepository.findAllByArticleId(id);
        if (!ligneVentes.isEmpty()) {
            throw new InvalidEntity("Impossible de supprimer un article déjà utilisé dans des ventes", ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }
        // Suppression de l'article
        articleRepository.deleteById(id);
    }
}
