package com.misaico.inventario.application.repository;

import com.misaico.inventario.application.entity.Producto;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductoRepository extends ReactiveCrudRepository<Producto, Integer> {
}
