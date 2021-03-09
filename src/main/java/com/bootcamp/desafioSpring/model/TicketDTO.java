package com.bootcamp.desafioSpring.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TicketDTO {

    Integer ticketId;
    List<ArticleDTO> articles;
    Double cost;
}
