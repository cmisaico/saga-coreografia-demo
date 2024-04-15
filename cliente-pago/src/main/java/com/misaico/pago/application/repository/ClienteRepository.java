package com.misaico.pago.application.repository;

import com.misaico.pago.application.entity.Cliente;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClienteRepository extends ReactiveCrudRepository<Cliente, Integer> {
}
