package com.bootcamp.desafioSpring.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParamsDTO {

    private String name;
    private String category;
    private String brand;
    private Double price;
    private Boolean freeShiping;
    private Integer prestige;
    private Integer Order;
}
