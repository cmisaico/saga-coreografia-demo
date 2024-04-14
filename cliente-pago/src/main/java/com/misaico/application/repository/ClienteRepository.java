package com.misaico.application.repository;

import com.misaico.application.entity.Cliente;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClienteRepository extends ReactiveCrudRepository<Cliente, Integer> {
}
