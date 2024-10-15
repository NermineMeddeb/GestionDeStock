package Mts.Crud.Services;

import java.util.List;

import Mts.Crud.Dto.ArticleDto;
import Mts.Crud.Dto.LigneCommandeClientDto;
import Mts.Crud.Dto.LigneCommandeFournisseurDto;
import Mts.Crud.Dto.LigneVenteDto;

public interface ArticleService {
    ArticleDto save(ArticleDto dto);//elle permeet de faire l'ajout et la modif d'un article
    ArticleDto findById(Integer id);
    ArticleDto findByCodeArticle(String codeArticle);
List<ArticleDto> findAll();

  List<LigneVenteDto> findHistoriqueVentes(Integer idArticle);

  List<LigneCommandeClientDto> findHistoriqueCommandeClient(Integer idArticle);

  List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle);

  List<ArticleDto> findAllArticleByIdCategory(Integer idCategory);

  void delete(Integer id);


}
