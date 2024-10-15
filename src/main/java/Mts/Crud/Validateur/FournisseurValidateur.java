package Mts.Crud.Validateur;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

import Mts.Crud.Dto.FournisseurDto;
public class FournisseurValidateur {
    public static List<String> validate(FournisseurDto dto) {
        List<String> errors = new ArrayList<>(); // Liste pour stocker les messages d'erreur

        // Vérifie si l'objet FournisseurDto est null
        if (dto == null) {
          // Si l'objet est null, ajoutez des messages d'erreur pour chaque champ obligatoire manquant
          errors.add("Veuillez renseigner le nom du fournisseur");
          errors.add("Veuillez renseigner le prénom du fournisseur");
          errors.add("Veuillez renseigner l'email du fournisseur");
          errors.add("Veuillez renseigner le numéro de téléphone du fournisseur");
          // Appelle le validateur d'adresse avec un objet null pour ajouter les erreurs d'adresse
          errors.addAll(AdresseValidateur.validate(null));
          return errors; // Retourne la liste d'erreurs
        }
    
        // Vérifie si le champ nom a une longueur (non vide et non null)
        if (!StringUtils.hasLength(dto.getNom())) {
          errors.add("Veuillez renseigner le nom du fournisseur"); // Ajoute un message d'erreur si le champ est vide
        }
    
        // Vérifie si le champ prénom a une longueur (non vide et non null)
        if (!StringUtils.hasLength(dto.getPrenom())) {
          errors.add("Veuillez renseigner le prénom du fournisseur"); // Ajoute un message d'erreur si le champ est vide
        }
    
        // Vérifie si le champ email a une longueur (non vide et non null)
        if (!StringUtils.hasLength(dto.getMail())) {
          errors.add("Veuillez renseigner l'email du fournisseur"); // Ajoute un message d'erreur si le champ est vide
        }
    
        // Vérifie si le champ numéro de téléphone a une longueur (non vide et non null)
        if (!StringUtils.hasLength(dto.getNumTel())) {
          errors.add("Veuillez renseigner le numéro de téléphone du fournisseur"); // Ajoute un message d'erreur si le champ est vide
        }
    
        // Appelle le validateur d'adresse avec l'adresse du fournisseur pour ajouter les erreurs d'adresse
        errors.addAll(AdresseValidateur.validate(dto.getAdresse()));
    
        return errors; // Retourne la liste de messages d'erreur
      }
}
