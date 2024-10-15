package Mts.Crud.Model;

import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// annotation qui nous permet de gerer des getters and des setters et des constructeur avec et sans args
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

// JPA annotation to mark this class as an entity to be mapped to a database table
@Entity
// JPA annotation to specify the table name in the database
@Table(name = "article")
public class Article extends EntiteAbstraite {

  // JPA annotation to specify the column name in the table and map it to this field
  @Column(name = "codearticle")
  private String codeArticle;

  @Column(name = "designation")
  private String designation;

  @Column(name = "prixunitaireht")
  private BigDecimal prixUnitaireHt;

  @Column(name = "tauxtva")
  private BigDecimal tauxTva;

  @Column(name = "prixunitairettc")
  private BigDecimal prixUnitaireTtc;

  @Column(name = "photo")
  private String photo;

  @Column(name = "identreprise")
  private Integer idEntreprise;

  // JPA annotation to specify many-to-one relationship with another entity
  @ManyToOne
  // JPA annotation to specify the join column for the relationship
  @JoinColumn(name = "idcategory")
  private Category category;

  // JPA annotation to specify one-to-many relationship with another entity
  @OneToMany(mappedBy = "article")
  private List<LigneVente> ligneVentes;

  @OneToMany(mappedBy = "article")
  private List<LigneCommandeClient> ligneCommandeClients;

  @OneToMany(mappedBy = "article")
  private List<LigneCommandeFournisseur> ligneCommandeFournisseurs;

  @OneToMany(mappedBy = "article")
  private List<MvtStk> mvtStk;

}
