package Mts.Crud.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Mts.Crud.Model.LigneCommandeClient;


public interface LigneCommandeClientRepository extends JpaRepository<LigneCommandeClient, Integer> {

    List<LigneCommandeClient> findAllByArticleId(Integer id);

    List<LigneCommandeClient> findAllByCommandeClientId(Integer id);

    
}
