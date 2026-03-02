package com.av1.IndicieHashEstatico.models;

import lombok.Data;

@Data
public class EntradaIndice {

    private String chave;
    private int pagina;

    public EntradaIndice(String chave, int pagina) {
        this.chave = chave;
        this.pagina = pagina;
    }

}
