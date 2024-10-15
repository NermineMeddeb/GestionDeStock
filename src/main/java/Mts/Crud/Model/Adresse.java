package Mts.Crud.Model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Utilise Lombok pour générer les getters, setters, et constructeurs
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

// Indique que cette classe est un type embarquable
@Embeddable
public class Adresse implements Serializable {

    @Column(name = "adresse1")
    @NotBlank(message = "L'adresse 1 ne doit pas être vide")
    private String adresse1;

    @Column(name = "adresse2")
    private String adresse2;

    @Column(name = "ville")
    @NotBlank(message = "La ville ne doit pas être vide")
    private String ville;

    @Column(name = "codepostale")
    @NotBlank(message = "Le code postal ne doit pas être vide")
    @Pattern(regexp = "\\d{5}", message = "Le code postal doit contenir 5 chiffres")
    private String codePostale;

    @Column(name = "pays")
    @NotBlank(message = "Le pays ne doit pas être vide")
    private String pays;
}
