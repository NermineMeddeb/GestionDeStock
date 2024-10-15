/*package Mts.Crud.Services;

import java.util.List;
import org.springframework.stereotype.Service;


import org.springframework.beans.factory.annotation.Autowired;

import Mts.Crud.Model.Produits;
import Mts.Crud.Repository.ProduitsRepository;

@Service
public class ProduitsService {

    @Autowired
    private ProduitsRepository produitsRepository;

    public List<Produits> getProduits() {
        return (List<Produits>) produitsRepository.findAll();
    }

    public Produits getProduits(int id) {
        return produitsRepository.findById(id).orElse(null);
    }

    public void deleteProduit(int id) {
        produitsRepository.deleteById(id);
    }

    public void addProduit(Produits produit) {
        produitsRepository.save(produit);
    }

    public void updateProduit(Produits produit, int id) {
        if (produitsRepository.existsById(id)) {
            produit.setId(id);
            produitsRepository.save(produit);
        }
    }
}
*/