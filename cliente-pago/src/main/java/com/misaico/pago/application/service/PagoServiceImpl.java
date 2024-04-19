package com.misaico.pago.application.service;


import com.misaico.pago.application.entity.Cliente;
import com.misaico.pago.application.entity.ClientePago;
import com.misaico.pago.application.mapper.EntityDtoMapper;
import com.misaico.pago.application.repository.ClienteRepository;
import com.misaico.pago.application.repository.PagoRepository;
import com.misaico.pago.common.dto.PagoDto;
import com.misaico.pago.common.dto.PagoProcessRequest;
import com.misaico.pago.common.exception.ClienteNoEncontradoException;
import com.misaico.pago.common.exception.InsuficienteSaldoException;
import com.misaico.pago.common.service.PagoService;
import com.misaico.common.events.pago.PagoEstado;
import com.misaico.common.utils.DuplicadoEventoValidador;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoServiceImpl.class);
    private static final Mono<Cliente> CLIENTE_NO_ENCONTRADO = Mono.error(new ClienteNoEncontradoException());
    private static final Mono<Cliente> SALDO_INSUFICIENTE = Mono.error(new InsuficienteSaldoException());
    private final ClienteRepository clienteRepository;
    private final PagoRepository pagoRepository;


    @Override
    @Transactional
    public Mono<PagoDto> procesar(PagoProcessRequest request) {
        return DuplicadoEventoValidador.validar(
                this.pagoRepository.existsByOrdenId(request.ordenId()),
                this.clienteRepository.findById(request.clienteId())
        ).switchIfEmpty(CLIENTE_NO_ENCONTRADO)
                .filter(cliente -> cliente.getSaldo() >= request.importe())
                .switchIfEmpty(SALDO_INSUFICIENTE)
                .flatMap(c -> this.descontarPago(c, request))
                .doOnNext(dto -> log.info("Pago procesado: {}", dto.ordenId()));

    }

    private Mono<PagoDto> descontarPago(Cliente cliente, PagoProcessRequest request){
        var clientePago = EntityDtoMapper.toClientePago(request);
        cliente.setSaldo(cliente.getSaldo() - request.importe());
        clientePago.setEstado(PagoEstado.DESCONTADO);
        return this.clienteRepository.save(cliente)
                .then(this.pagoRepository.save(clientePago))
                .map(EntityDtoMapper::toDto);
    }

    @Override
    @Transactional
    public Mono<PagoDto> reembolsar(UUID ordenId) {
        return this.pagoRepository.findByOrdenIdAndEstado(ordenId, PagoEstado.DESCONTADO)
                .zipWhen(cp -> this.clienteRepository.findById(cp.getClienteId()))
                .flatMap(t -> this.reembolsarPago(t.getT1(), t.getT2()))
                .doOnNext(dto -> log.info("reembolsado saldo {} para {}", dto.importe(), dto.ordenId()));
    }

    private Mono<PagoDto> reembolsarPago(ClientePago clientePago, Cliente cliente){
        cliente.setSaldo(cliente.getSaldo() + clientePago.getImporte());
        clientePago.setEstado(PagoEstado.REEMBOLSADO);
        return this.clienteRepository.save(cliente)
                .then(this.pagoRepository.save(clientePago))
                .map(EntityDtoMapper::toDto);
    }
}
