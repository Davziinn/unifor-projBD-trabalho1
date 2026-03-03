package com.av1.IndicieHashEstatico.dto;

import lombok.Data;

@Data
public class ResumoPaginasResponseDTO {
    private final int totalPaginas;
    private final PaginaResumoDTO primeiraPagina;
    private final PaginaResumoDTO ultimaPagina;

    public ResumoPaginasResponseDTO(int totalPaginas, PaginaResumoDTO primeiraPagina, PaginaResumoDTO ultimaPagina) {
        this.totalPaginas = totalPaginas;
        this.primeiraPagina = primeiraPagina;
        this.ultimaPagina = ultimaPagina;
    }
}
