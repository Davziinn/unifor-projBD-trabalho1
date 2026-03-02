package com.av1.IndicieHashEstatico.dto;


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

    public boolean isEncontrada() {
        return encontrada;
    }

    public int getPagina() {
        return pagina;
    }

    public int getCusto() {
        return custo;
    }

    public long getTempoNano() {
        return tempoNano;
    }
}
