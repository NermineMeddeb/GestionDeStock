package Mts.Crud.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Mts.Crud.Model.Entreprise;


public interface EntrepriseRepository extends JpaRepository<Entreprise, Integer> {
    
}
