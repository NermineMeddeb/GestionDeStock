package Mts.Crud.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Mts.Crud.Model.Fournisseur;


public interface FournisseurRepository extends JpaRepository<Fournisseur, Integer> {
    
}
