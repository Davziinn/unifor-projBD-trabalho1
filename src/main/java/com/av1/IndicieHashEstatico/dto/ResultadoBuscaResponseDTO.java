package com.av1.IndicieHashEstatico.dto;

import lombok.Data;

@Data
public class ResultadoBuscaResponseDTO {
    private final boolean encontrada;
    private final int pagina;
    private final int custo;
    private final long tempoNano;

    public ResultadoBuscaResponseDTO(boolean encontrada, int pagina, int custo, long tempoNano) {
        this.encontrada = encontrada;
        this.pagina = pagina;
        this.custo = custo;
        this.tempoNano = tempoNano;
    }
}
