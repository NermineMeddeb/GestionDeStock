package Mts.Crud.Dto;



import Mts.Crud.Model.CommandeFournisseur;
import Mts.Crud.Model.EtatCommande;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommandeFournisseurDto {

  private Integer id;

  private String code;

  private LocalDate dateCommande;

  private EtatCommande etatCommande;

  private FournisseurDto fournisseur;

  private Integer idEntreprise;

  private List<LigneCommandeFournisseurDto> ligneCommandeFournisseurs;

  public static CommandeFournisseurDto fromEntity(CommandeFournisseur commandeFournisseur) {
    if (commandeFournisseur == null) {
      return null;
    }
    return CommandeFournisseurDto.builder()
        .id(commandeFournisseur.getId())
        .code(commandeFournisseur.getCode())
        .dateCommande(commandeFournisseur.getDateCommande())
        .fournisseur(FournisseurDto.fromEntity(commandeFournisseur.getFournisseur()))
        .etatCommande(commandeFournisseur.getEtatCommande())
        .idEntreprise(commandeFournisseur.getIdEntreprise())
        .build();
  }

  public static CommandeFournisseur toEntity(CommandeFournisseurDto dto) {
    if (dto == null) {
      return null;
    }
    CommandeFournisseur commandeFournisseur = new CommandeFournisseur();
    commandeFournisseur.setId(dto.getId());
    commandeFournisseur.setCode(dto.getCode());
    commandeFournisseur.setDateCommande(dto.getDateCommande());
    commandeFournisseur.setFournisseur(FournisseurDto.toEntity(dto.getFournisseur()));
    commandeFournisseur.setIdEntreprise(dto.getIdEntreprise());
    commandeFournisseur.setEtatCommande(dto.getEtatCommande());
    return commandeFournisseur;
  }

  public boolean isCommandeLivree() {
    return EtatCommande.LIVREE.equals(this.etatCommande);
  }

}