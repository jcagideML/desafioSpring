package com.bootcamp.desafioSpring.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ProductDTO {

    private Integer productId;
    private String name;
    private String category;
    private String brand;
    private Double price;
    private Integer quantity;
    private Boolean freeShiping;
    private Integer prestige;

    public ProductDTO() {
    }

    public ProductDTO(String name, String category, String brand, Double price, Integer quantity, Boolean freeShiping, Integer prestige) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.freeShiping = freeShiping;
        this.prestige = prestige;
    }
}
