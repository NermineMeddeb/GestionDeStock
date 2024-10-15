package Mts.Crud.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Mts.Crud.Model.Article;

import java.util.List;
import java.util.Optional;


public interface ArticleRepository extends JpaRepository<Article, Integer> {

Optional <Article> findByCodeArticle(String codeArticle);

List<Article> findAllByCategoryId(Integer id);
    
}