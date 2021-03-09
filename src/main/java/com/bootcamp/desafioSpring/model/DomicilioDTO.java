package com.bootcamp.desafioSpring.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DomicilioDTO {
    String calle;
    Integer altura;
    String localidad;
    String provincia;
    String extras;
    Integer codPostal;
}
