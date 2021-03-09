package com.bootcamp.desafioSpring.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDTO {
    Integer productId;
    String name;
    String brand;
    Integer quantity;
}
