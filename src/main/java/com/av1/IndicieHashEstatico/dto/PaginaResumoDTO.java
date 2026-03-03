package com.av1.IndicieHashEstatico.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaginaResumoDTO {
    private final int numero;
    private final List<String> primeiros5Registros;

    public PaginaResumoDTO(int numero, List<String> primeiros5Registros) {
        this.numero = numero;
        this.primeiros5Registros = primeiros5Registros;
    }
}
