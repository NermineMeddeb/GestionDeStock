package Mts.Crud.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Mts.Crud.Model.LigneVente;


public interface LigneVenteRepository extends JpaRepository<LigneVente, Integer> {

    List<LigneVente> findAllByArticleId(Integer idArticle);
    
}
