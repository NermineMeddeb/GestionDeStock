package Mts.Crud.Repository;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import Mts.Crud.Model.MvtStk;


public interface MvtStockRepository extends JpaRepository <MvtStk, Integer>{




    Collection<MvtStk> findAllByArticleId(Integer idArticle);
    
}
