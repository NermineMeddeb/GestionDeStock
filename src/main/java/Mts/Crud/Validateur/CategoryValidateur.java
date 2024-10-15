package Mts.Crud.Validateur;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

import Mts.Crud.Dto.CategoryDto;

public class CategoryValidateur {
    public static List<String> validate(CategoryDto categoryDto) {
    List<String> errors = new ArrayList<>();

    if (categoryDto == null || !StringUtils.hasLength(categoryDto.getCode())) {
      errors.add("Veuillez renseigner le code de la categorie");
    }
    return errors;
  }

}
