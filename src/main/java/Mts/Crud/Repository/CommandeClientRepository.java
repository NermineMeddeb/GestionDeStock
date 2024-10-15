package Mts.Crud.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Mts.Crud.Model.CommandeClient;

public interface CommandeClientRepository extends JpaRepository<CommandeClient, Integer> {

    List<CommandeClient> findAllByClientId(Integer id);

    Optional<CommandeClient> findCommandeClientByCode(String code);
    
}
