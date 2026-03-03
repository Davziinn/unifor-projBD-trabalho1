package com.av1.IndicieHashEstatico.dto;

import lombok.Data;

@Data
public class ConstruirIndiceResponseDTO {
    private final int totalRegistros;
    private final int totalPaginas;
    private final long tempoConstrucaoNano;

    public ConstruirIndiceResponseDTO(int totalRegistros, int totalPaginas, long tempoConstrucaoNano) {
        this.totalRegistros = totalRegistros;
        this.totalPaginas = totalPaginas;
        this.tempoConstrucaoNano = tempoConstrucaoNano;
    }
}
