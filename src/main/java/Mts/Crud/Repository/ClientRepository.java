package Mts.Crud.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import Mts.Crud.Model.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {
}