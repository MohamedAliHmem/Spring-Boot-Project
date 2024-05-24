package isetn.dsi22.test;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private int qteStock;

    private int qteSousStock;

    @ManyToOne
    @JoinColumn(name = "idStock", nullable = true)
    private Stock stock;

    @ManyToOne
    @JoinColumn(name = "idSousStock", nullable = true)
    private SousStock sousStock;

    public Produit() {
        super();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getQteStock() {
        return qteStock;
    }

    public void setQteStock(int qteStock) {
        this.qteStock = qteStock;
    }

    public int getQteSousStock() {
        return qteSousStock;
    }

    public void setQteSousStock(int qteSousStock) {
        this.qteSousStock = qteSousStock;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public SousStock getSousStock() {
        return sousStock;
    }

    public void setSousStock(SousStock sousStock) {
        this.sousStock = sousStock;
    }
    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", qteStock=" + qteStock +
                ", qteSousStock=" + qteSousStock +
                ", stock=" + stock +
                ", sousStock=" + sousStock +
                '}';
    }
}
