package com.av1.IndicieHashEstatico.dto;

import lombok.Data;

@Data
public class ConstruirIndiceResponseDTO {
    private final int totalRegistros;
    private final int totalPaginas;
    private final int numeroBuckets;
    private final int capacidadeBucket;
    private final long tempoConstrucaoNano;

    public ConstruirIndiceResponseDTO(int totalRegistros, int totalPaginas, int numeroBuckets, int capacidadeBucket, long tempoConstrucaoNano) {
        this.totalRegistros = totalRegistros;
        this.totalPaginas = totalPaginas;
        this.numeroBuckets = numeroBuckets;
        this.capacidadeBucket = capacidadeBucket;
        this.tempoConstrucaoNano = tempoConstrucaoNano;
    }
}
