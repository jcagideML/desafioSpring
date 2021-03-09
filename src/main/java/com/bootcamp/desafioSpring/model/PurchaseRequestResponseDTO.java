package com.bootcamp.desafioSpring.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class PurchaseRequestResponseDTO {
    TicketDTO ticket;
    HttpStatus status;
}
