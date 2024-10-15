///projet 9dim 
package Mts.Crud.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Produits {

    public enum Color {
        RED,
        GREEN,
        BLUE,
        YELLOW
    }

    @Id
    private int id;
    private String model;
    private String brand;

    @Column(name = "released_year")
    private int year;

    @Enumerated(EnumType.STRING)
    private Color color;

    // Constructeur par défaut requis par JPA
    public Produits() {
    }

    public Produits(int id, String model, String brand, int year, Color color) {
        this.id = id;
        this.model = model;
        this.brand = brand;
        this.year = year;
        this.color = color;

    }

    @Override
    public String toString() {
        return "Produits [id=" + id + ", model=" + model + ", brand=" + brand + ", year=" + year + ", color=" + color + "]";
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
