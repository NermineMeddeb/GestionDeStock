package Mts.Crud.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Mts.Crud.Model.Roles;


public interface RoleRepository extends JpaRepository<Roles, Integer> {
    
}
