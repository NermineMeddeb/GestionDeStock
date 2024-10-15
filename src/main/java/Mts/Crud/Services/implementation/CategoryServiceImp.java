package Mts.Crud.Services.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import Mts.Crud.Dto.CategoryDto;
import Mts.Crud.Model.Article;
import Mts.Crud.Repository.ArticleRepository;
import Mts.Crud.Repository.CategoryRepository;
import Mts.Crud.Services.CategoryService;
import Mts.Crud.Validateur.CategoryValidateur;
import Mts.Crud.Exceptions.InvalidEntity;
import Mts.Crud.Exceptions.InvalidOperation;
import Mts.Crud.Exceptions.EntityNotFound;
import Mts.Crud.Exceptions.ErrorCodes;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public CategoryServiceImp(CategoryRepository categoryRepository, ArticleRepository articleRepository) {
        this.categoryRepository = categoryRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public CategoryDto save(CategoryDto dto) {
        List<String> errors = CategoryValidateur.validate(dto);
        if (!errors.isEmpty()) {
            // Log des erreurs de validation
            log.error("La catégorie n'est pas valide {}", dto);
            // Lancer une exception en cas d'erreurs de validation
            throw new InvalidEntity("La catégorie n'est pas valide", ErrorCodes.CATEGORY_NOT_VALID, errors);
        }
        // Convertir le DTO en entité, sauvegarder et convertir l'entité sauvegardée en DTO
        return CategoryDto.fromEntity(
            categoryRepository.save(CategoryDto.toEntity(dto))
        );
    }

    @Override
    public CategoryDto findById(Integer id) {
        if (id == null) {
            // Log de l'erreur pour ID null
            log.error("L'ID de la catégorie est null");
            return null;
        }
        return categoryRepository.findById(id)
            .map(CategoryDto::fromEntity)
            .orElseThrow(() -> new EntityNotFound(
                "Aucune catégorie avec l'ID = " + id + " n'a été trouvée dans la BDD",
                ErrorCodes.CATEGORY_NOT_FOUND)
            );
    }

    @Override
    public CategoryDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            // Log de l'erreur pour code null ou vide
            log.error("Le code de la catégorie est null ou vide");
            return null;
        }
        return categoryRepository.findCategoryByCode(code)
            .map(CategoryDto::fromEntity)
            .orElseThrow(() -> new EntityNotFound(
                "Aucune catégorie avec le CODE = " + code + " n'a été trouvée dans la BDD",
                ErrorCodes.CATEGORY_NOT_FOUND)
            );
    }

    @Override
    public List<CategoryDto> findAll() {
        // Rechercher toutes les catégories et les convertir en DTOs
        return categoryRepository.findAll().stream()
            .map(CategoryDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            // Log de l'erreur pour ID null
            log.error("L'ID de la catégorie est null");
            return;
        }
        List<Article> articles = articleRepository.findAllByCategoryId(id);
        if (!articles.isEmpty()) {
            // Lancer une exception si la catégorie est utilisée dans des articles
            throw new InvalidOperation("Impossible de supprimer cette catégorie qui est déjà utilisée",
                ErrorCodes.CATEGORY_ALREADY_IN_USE);
        }
        // Supprimer la catégorie par ID
        categoryRepository.deleteById(id);
    }
}
