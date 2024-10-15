package Mts.Crud.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Mts.Crud.Model.Category;


public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findCategoryByCode(String code);
    


}
