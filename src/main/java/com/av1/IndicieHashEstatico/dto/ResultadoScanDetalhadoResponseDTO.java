package com.av1.IndicieHashEstatico.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResultadoScanDetalhadoResponseDTO {
    private final boolean encontrada;
    private final int pagina;
    private final int custo;
    private final long tempoNano;
    private final List<String> registrosLidos;
    private final boolean atingiuLimite;

    public ResultadoScanDetalhadoResponseDTO(boolean encontrada, int pagina, int custo, long tempoNano,
                                             List<String> registrosLidos, boolean atingiuLimite) {
        this.encontrada = encontrada;
        this.pagina = pagina;
        this.custo = custo;
        this.tempoNano = tempoNano;
        this.registrosLidos = registrosLidos;
        this.atingiuLimite = atingiuLimite;
    }
}
