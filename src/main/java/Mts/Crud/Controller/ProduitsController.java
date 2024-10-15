/*package Mts.Crud.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Mts.Crud.Model.Produits;
import Mts.Crud.Services.ProduitsService;

@RestController
public class ProduitsController {
    // Le service ProduitsService est injecté pour être utilisé dans les méthodes du contrôleur.
    @Autowired
    private ProduitsService produitsService;

    // Récupérer la liste des produits.
    @GetMapping("/crud")
    public List<Produits> getProduits() {
        return produitsService.getProduits();
    }

    // Récupérer un produit spécifique par son ID.
    @GetMapping("/crud/{id}")
    public Produits getProduitById(@PathVariable int id) {
        return produitsService.getProduits(id);
    }

    // Supprimer un produit spécifique par son ID.
    @RequestMapping(value = "/crud/{id}", method = RequestMethod.DELETE)
    public void deleteProduit(@PathVariable int id) {
        produitsService.deleteProduit(id);
    }

    // Ajouter un nouveau produit.
    @RequestMapping(value = "/crud", method = RequestMethod.POST)
    public void addProduit(@RequestBody Produits produit) {
        produitsService.addProduit(produit);
    }
     // Mettre à jour un produit existant.
    @PutMapping("/crud/{id}")
    public void updateProduit(@RequestBody Produits produit, @PathVariable int id) {
        produitsService.updateProduit(produit,id);


}}
*/