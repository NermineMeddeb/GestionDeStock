package Mts.Crud.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Mts.Crud.Model.LigneCommandeFournisseur;



public interface LigneCommandeFournisseurRepository extends JpaRepository<LigneCommandeFournisseur, Integer> {
        List<LigneCommandeFournisseur> findAllByArticleId(Integer id);
        List<LigneCommandeFournisseur> findAllByCommandeFournisseurId(Integer id);

        
}
