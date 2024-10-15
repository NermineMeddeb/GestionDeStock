package Mts.Crud.Dto;

import Mts.Crud.Model.Adresse;
import lombok.Builder;
import lombok.Data;

@Data // Génère automatiquement les getters, setters, equals, hashCode, et toString.
@Builder // Génère un constructeur de type builder pour l'initialisation fluide des objets.
public class AdresseDto {

  // Champs représentant les différentes parties d'une adresse
  private String adresse1;
  private String adresse2;
  private String ville;
  private String codePostale;
  private String pays;

  /**
   * Convertit une entité Adresse en un DTO AdresseDto.
   * 
   * @param adresse l'entité Adresse à convertir
   * @return l'objet AdresseDto correspondant ou null si l'entité est null
   */
  public static AdresseDto fromEntity(Adresse adresse) {
    if (adresse == null) {
      return null; // Vérifie si l'entité est null pour éviter les NullPointerExceptions
    }

    // Utilise le builder pour créer une instance de AdresseDto à partir de l'entité Adresse
    return AdresseDto.builder()
        .adresse1(adresse.getAdresse1())
        .adresse2(adresse.getAdresse2())
        .codePostale(adresse.getCodePostale())
        .ville(adresse.getVille())
        .pays(adresse.getPays())
        .build();
  }

  /**
   * Convertit un DTO AdresseDto en une entité Adresse.
   * 
   * @param adresseDto le DTO AdresseDto à convertir
   * @return l'entité Adresse correspondante ou null si le DTO est null
   */
  public static Adresse toEntity(AdresseDto adresseDto) {
    if (adresseDto == null) {
      return null; // Vérifie si le DTO est null pour éviter les NullPointerExceptions
    }

    // Crée une nouvelle instance de l'entité Adresse et copie les valeurs du DTO
    Adresse adresse = new Adresse();
    adresse.setAdresse1(adresseDto.getAdresse1());
    adresse.setAdresse2(adresseDto.getAdresse2());
    adresse.setCodePostale(adresseDto.getCodePostale());
    adresse.setVille(adresseDto.getVille());
    adresse.setPays(adresseDto.getPays());
    return adresse;
  }

}
