package Mts.Crud.Dto;

import java.math.BigDecimal;

import Mts.Crud.Model.Article;
import lombok.Builder;
import lombok.Data;
//Permet de créer des instances de ArticleDto à l'aide d'un pattern de construction fluide (builder pattern)
@Builder
@Data
public class ArticleDto {

  private Integer id;

  private String codeArticle;

  private String designation;//Description ou nom de l'article.

  private BigDecimal prixUnitaireHt;// Prix unitaire hors taxes.

  private BigDecimal tauxTva;//Taux de TVA applicable.

  private BigDecimal prixUnitaireTtc;//Prix unitaire toutes taxes comprises.

  private String photo;

  private CategoryDto category;//Catégorie associée à l'article, représentée par un autre DTO.

  private Integer idEntreprise;// Identifiant de l'entreprise associée à l'article.

 //convertit une instance de l'entité Article en un ArticleDto.
public static ArticleDto fromEntity(Article article) {
    if (article == null) {
      return null;
    }
    return ArticleDto.builder()
        .id(article.getId())
        .codeArticle(article.getCodeArticle())
        .designation(article.getDesignation())
        .photo(article.getPhoto())
        .prixUnitaireHt(article.getPrixUnitaireHt())
        .prixUnitaireTtc(article.getPrixUnitaireTtc())
        .tauxTva(article.getTauxTva())
        .idEntreprise(article.getIdEntreprise())
        .category(CategoryDto.fromEntity(article.getCategory()))
        .build();
  }

  public static Article toEntity(ArticleDto articleDto) {
    if (articleDto == null) {
      return null;
    }
    Article article = new Article();
    article.setId(articleDto.getId());
    article.setCodeArticle(articleDto.getCodeArticle());
    article.setDesignation(articleDto.getDesignation());
    article.setPhoto(articleDto.getPhoto());
    article.setPrixUnitaireHt(articleDto.getPrixUnitaireHt());
    article.setPrixUnitaireTtc(articleDto.getPrixUnitaireTtc());
    article.setTauxTva(articleDto.getTauxTva());
    article.setIdEntreprise(articleDto.getIdEntreprise());
    article.setCategory(CategoryDto.toEntity(articleDto.getCategory()));
    return article;
  }

}
