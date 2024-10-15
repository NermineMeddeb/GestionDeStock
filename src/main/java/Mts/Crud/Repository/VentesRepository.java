package Mts.Crud.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Mts.Crud.Model.Ventes;

public interface VentesRepository extends JpaRepository<Ventes, Integer> {

   // List<Ventes> findAllByArticleId(Integer id);
    Optional<Ventes> findVentesByCode(String code);
    

}
