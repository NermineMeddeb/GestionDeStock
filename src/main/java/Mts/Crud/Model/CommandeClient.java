package Mts.Crud.Model;

import java.time.Instant;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "commandeclient")
public class CommandeClient extends EntiteAbstraite {

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "datecommande", nullable = false)
    private Instant dateCommande;

    @Enumerated(EnumType.STRING)
    @Column(name = "etatcommande", nullable = false)
    private EtatCommande etatCommande;

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idclient", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "commandeClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneCommandeClient> ligneCommandeClients;

    public void setEtatCommande(EtatCommande etatCommande2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEtatCommande'");
    }

}
