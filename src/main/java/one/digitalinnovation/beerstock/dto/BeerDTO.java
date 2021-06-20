package one.digitalinnovation.beerstock.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import one.digitalinnovation.beerstock.enums.BeerType;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class BeerDTO implements Serializable {

    private Long id;
    private String nome;
    private String brand;
    private int max;
    private int quantity;
    private BeerType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BeerType getType() {
        return type;
    }

    public void setType(BeerType type) {
        this.type = type;
    }
}
