package Mts.Crud.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import Mts.Crud.Dto.CommandeFournisseurDto;
import Mts.Crud.Dto.LigneCommandeFournisseurDto;
import Mts.Crud.Model.EtatCommande;
import Mts.Crud.Services.CommandeFournisseurService;
import Mts.Crud.Controller.Api.CommandeFournisseurApi;



@RestController
public class CommandeFournisseurController implements CommandeFournisseurApi {

  private CommandeFournisseurService CommandeFournisseurService;

  @Autowired
  public CommandeFournisseurController(CommandeFournisseurService CommandeFournisseurService) {
    this.CommandeFournisseurService = CommandeFournisseurService;
  }

  @Override
  public CommandeFournisseurDto save(CommandeFournisseurDto dto) {
    return CommandeFournisseurService.save(dto);
  }

  @Override
  public CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
    return CommandeFournisseurService.updateEtatCommande(idCommande, etatCommande);
  }

  @Override
  public CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
    return CommandeFournisseurService.updateQuantiteCommande(idCommande, idLigneCommande, quantite);
  }

  @Override
  public CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
    return CommandeFournisseurService.updateFournisseur(idCommande, idFournisseur);
  }

  @Override
  public CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
    return CommandeFournisseurService.updateArticle(idCommande, idLigneCommande, idArticle);
  }

  @Override
  public CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
    return CommandeFournisseurService.deleteArticle(idCommande, idLigneCommande);
  }

  @Override
  public CommandeFournisseurDto findById(Integer id) {
    return CommandeFournisseurService.findById(id);
  }

  @Override
  public CommandeFournisseurDto findByCode(String code) {
    return CommandeFournisseurService.findByCode(code);
  }

  @Override
  public List<CommandeFournisseurDto> findAll() {
    return CommandeFournisseurService.findAll();
  }

  @Override
  public List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande) {
    return CommandeFournisseurService.findAllLignesCommandesFournisseurByCommandeFournisseurId(idCommande);
  }

  @Override
  public void delete(Integer id) {
    CommandeFournisseurService.delete(id);
  }    
}
