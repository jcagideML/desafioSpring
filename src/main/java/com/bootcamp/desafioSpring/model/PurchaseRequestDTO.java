package com.bootcamp.desafioSpring.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PurchaseRequestDTO {
    List<ArticleDTO> articles;
}
