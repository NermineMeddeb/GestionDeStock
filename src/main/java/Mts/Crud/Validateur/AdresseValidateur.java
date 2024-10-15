package Mts.Crud.Validateur;

  import java.util.ArrayList;
  import java.util.List;
  import org.springframework.util.StringUtils;

import Mts.Crud.Dto.AdresseDto;
  
  public class AdresseValidateur {
  
    public static List<String> validate(AdresseDto adresseDto) {
      List<String> errors = new ArrayList<>(); // Liste pour stocker les messages d'erreur

      // Vérifie si l'objet adresseDto est null
      if (adresseDto == null) {
        // Si l'objet est null, ajoutez des messages d'erreur pour chaque champ obligatoire manquant
        errors.add("L'adresse 1 est obligatoire.");
        errors.add("La ville est obligatoire.");
        errors.add("Le pays est obligatoire.");
        errors.add("Le code postal est obligatoire.");
        return errors; // Retourne la liste d'erreurs
      }
  
      // Vérifie si le champ adresse1 a une longueur (non vide et non null)
      if (!StringUtils.hasLength(adresseDto.getAdresse1())) {
        errors.add("L'adresse 1 est obligatoire."); // Ajoute un message d'erreur si le champ est vide
      }
  
      // Vérifie si le champ ville a une longueur (non vide et non null)
      if (!StringUtils.hasLength(adresseDto.getVille())) {
        errors.add("La ville est obligatoire."); // Ajoute un message d'erreur si le champ est vide
      }
  
      // Vérifie si le champ pays a une longueur (non vide et non null)
      if (!StringUtils.hasLength(adresseDto.getPays())) {
        errors.add("Le pays est obligatoire."); // Ajoute un message d'erreur si le champ est vide
      }
  
      // Vérifie si le champ codePostale a une longueur (non vide et non null)
      if (!StringUtils.hasLength(adresseDto.getCodePostale())) {
        errors.add("Le code postal est obligatoire."); // Ajoute un message d'erreur si le champ est vide
      }
  
      return errors; // Retourne la liste de messages d'erreur
    }
  }
