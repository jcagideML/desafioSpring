package com.bootcamp.desafioSpring.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ClientDTO {

    private Integer clientId;
    private String nombre;
    private String apellido;
    private Integer dni;
    private Date nacimiento;
    private DomicilioDTO domicilio;
    private String email;
}
