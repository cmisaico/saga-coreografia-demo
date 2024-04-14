package com.misaico.application.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Cliente {

    @Id
    private Integer id;
    private String nombre;
    private Integer saldo;
}
