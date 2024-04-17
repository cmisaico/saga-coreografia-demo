package com.misaico.orden.common.service;

import com.misaico.orden.common.dto.OrdenCompraDto;

public interface OrdenEventoListener {

    void emitirOrdenCreado(OrdenCompraDto ordenCompraDto);
}
