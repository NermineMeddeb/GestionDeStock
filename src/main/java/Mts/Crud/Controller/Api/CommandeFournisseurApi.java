package Mts.Crud.Controller.Api;

import org.springframework.web.bind.annotation.*;
import Mts.Crud.Dto.CommandeFournisseurDto;
import Mts.Crud.Dto.LigneCommandeFournisseurDto;
import Mts.Crud.Model.EtatCommande;
import Mts.Crud.Utils.Constants;
import java.math.BigDecimal;
import java.util.List;




public interface CommandeFournisseurApi {

    @PostMapping(Constants.CREATE_COMMANDE_FOURNISSEUR_ENDPOINT)
    CommandeFournisseurDto save(@RequestBody CommandeFournisseurDto dto);

    @PatchMapping(Constants.COMMANDE_FOURNISSEUR_ENDPOINT + "/update/etat/{idCommande}/{etatCommande}")
    CommandeFournisseurDto updateEtatCommande(@PathVariable("idCommande") Integer idCommande,
                                              @PathVariable("etatCommande") EtatCommande etatCommande);

    @PatchMapping(Constants.COMMANDE_FOURNISSEUR_ENDPOINT + "/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
    CommandeFournisseurDto updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande,
                                                  @PathVariable("idLigneCommande") Integer idLigneCommande,
                                                  @PathVariable("quantite") BigDecimal quantite);

    @PatchMapping(Constants.COMMANDE_FOURNISSEUR_ENDPOINT + "/update/fournisseur/{idCommande}/{idFournisseur}")
    CommandeFournisseurDto updateFournisseur(@PathVariable("idCommande") Integer idCommande,
                                             @PathVariable("idFournisseur") Integer idFournisseur);

    @PatchMapping(Constants.COMMANDE_FOURNISSEUR_ENDPOINT + "/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
    CommandeFournisseurDto updateArticle(@PathVariable("idCommande") Integer idCommande,
                                         @PathVariable("idLigneCommande") Integer idLigneCommande,
                                         @PathVariable("idArticle") Integer idArticle);

    @DeleteMapping(Constants.COMMANDE_FOURNISSEUR_ENDPOINT + "/delete/article/{idCommande}/{idLigneCommande}")
    CommandeFournisseurDto deleteArticle(@PathVariable("idCommande") Integer idCommande,
                                         @PathVariable("idLigneCommande") Integer idLigneCommande);

    @GetMapping(Constants.FIND_COMMANDE_FOURNISSEUR_BY_ID_ENDPOINT)
    CommandeFournisseurDto findById(@PathVariable("idCommandeFournisseur") Integer id);

    @GetMapping(Constants.FIND_COMMANDE_FOURNISSEUR_BY_CODE_ENDPOINT)
    CommandeFournisseurDto findByCode(@PathVariable("codeCommandeFournisseur") String code);

    @GetMapping(Constants.FIND_ALL_COMMANDE_FOURNISSEUR_ENDPOINT)
    List<CommandeFournisseurDto> findAll();

    @GetMapping(Constants.COMMANDE_FOURNISSEUR_ENDPOINT + "/lignesCommande/{idCommande}")
    List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(@PathVariable("idCommande") Integer idCommande);

    @DeleteMapping(Constants.DELETE_COMMANDE_FOURNISSEUR_ENDPOINT)
    void delete(@PathVariable("idCommandeFournisseur") Integer id);
}
