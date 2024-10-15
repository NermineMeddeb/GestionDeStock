package Mts.Crud.Controller.Api;

import static Mts.Crud.Utils.Constants.APP_ROOT;

import Mts.Crud.Dto.ArticleDto;
import Mts.Crud.Dto.LigneCommandeClientDto;
import Mts.Crud.Dto.LigneCommandeFournisseurDto;
import Mts.Crud.Dto.LigneVenteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@Api(APP_ROOT + "/articles") // Cette annotation reste inchangée
public interface ArticleApi {

    @PostMapping(value = APP_ROOT + "/articles/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Enregistrer un article", description = "Cette méthode permet d'enregistrer ou modifier un article")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "L'article a été créé / modifié"),
        @ApiResponse(responseCode = "400", description = "L'article n'est pas valide")
    })
    ArticleDto save(@RequestBody ArticleDto dto);

    @GetMapping(value = APP_ROOT + "/articles/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rechercher un article par ID", description = "Cette méthode permet de chercher un article par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "L'article a été trouvé dans la base de données"),
        @ApiResponse(responseCode = "404", description = "Aucun article n'existe dans la base de données avec l'ID fourni")
    })
    ArticleDto findById(@PathVariable("idArticle") Integer id);

    @GetMapping(value = APP_ROOT + "/articles/filter/{codeArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rechercher un article par CODE", description = "Cette méthode permet de chercher un article par son CODE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "L'article a été trouvé dans la base de données"),
        @ApiResponse(responseCode = "404", description = "Aucun article n'existe dans la base de données avec le CODE fourni")
    })
    ArticleDto findByCodeArticle(@PathVariable("codeArticle") String codeArticle);

    @GetMapping(value = APP_ROOT + "/articles/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Renvoie la liste des articles", description = "Cette méthode permet de récupérer et renvoyer la liste des articles dans la base de données")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "La liste des articles ou une liste vide")
    })
    List<ArticleDto> findAll();

    @GetMapping(value = APP_ROOT + "/articles/historique/vente/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Historique des ventes d'un article", description = "Cette méthode permet de récupérer l'historique des ventes d'un article")
    List<LigneVenteDto> findHistoriqueVentes(@PathVariable("idArticle") Integer idArticle);

    @GetMapping(value = APP_ROOT + "/articles/historique/commandeclient/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Historique des commandes client pour un article", description = "Cette méthode permet de récupérer l'historique des commandes client pour un article")
    List<LigneCommandeClientDto> findHistoriqueCommandeClient(@PathVariable("idArticle") Integer idArticle);

    @GetMapping(value = APP_ROOT + "/articles/historique/commandefournisseur/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Historique des commandes fournisseur pour un article", description = "Cette méthode permet de récupérer l'historique des commandes fournisseur pour un article")
    List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(@PathVariable("idArticle") Integer idArticle);

    @GetMapping(value = APP_ROOT + "/articles/filter/category/{idCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Rechercher des articles par catégorie", description = "Cette méthode permet de chercher des articles par catégorie")
    List<ArticleDto> findAllArticleByIdCategory(@PathVariable("idCategory") Integer idCategory);

    @DeleteMapping(value = APP_ROOT + "/articles/delete/{idArticle}")
    @Operation(summary = "Supprimer un article", description = "Cette méthode permet de supprimer un article par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "L'article a été supprimé avec succès")
    })
    void delete(@PathVariable("idArticle") Integer id);
}
