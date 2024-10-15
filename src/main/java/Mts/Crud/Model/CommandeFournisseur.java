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
@Table(name = "commandefournisseur")
public class CommandeFournisseur extends EntiteAbstraite {

  @Column(name = "code")
  private String code;

  @Column(name = "datecommande")
  private Instant dateCommande;

  @Column(name = "etatcommande")
  @Enumerated(EnumType.STRING)
  private EtatCommande etatCommande;

  @Column(name = "identreprise")
  private Integer idEntreprise;

  @ManyToOne
  @JoinColumn(name = "idfournisseur")
  private Fournisseur fournisseur;

  @OneToMany(mappedBy = "commandeFournisseur")
  private List<LigneCommandeFournisseur> ligneCommandeFournisseurs;

public void setEtatCommande(EtatCommande etatCommande2) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setEtatCommande'");
}


}