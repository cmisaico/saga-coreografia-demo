package com.misaico.inventario.application.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Producto {

    @Id
    private Integer id;
    private String descripcion;
    private Integer cantidadDisponible;
}
