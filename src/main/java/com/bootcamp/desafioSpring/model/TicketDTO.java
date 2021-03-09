package com.bootcamp.desafioSpring.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TicketDTO {

    Integer solicitudId;
    List<ArticleDTO> articles;
    Double cost;
}
