package Mts.Crud.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Mts.Crud.Model.CommandeClient;
import Mts.Crud.Model.CommandeFournisseur;

public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur, Integer> {

    Optional<CommandeFournisseur> findCommandeFournisseurByCode(String code);
  
    List<CommandeClient> findAllByFournisseurId(Integer id);
  }