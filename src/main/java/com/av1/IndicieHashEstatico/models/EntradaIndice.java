package com.av1.IndicieHashEstatico.models;

public class EntradaIndice {

    private String chave;
    private int pagina;

    public EntradaIndice(String chave, int pagina) {
        this.chave = chave;
        this.pagina = pagina;
    }

    public String getChave() {
        return chave;
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }
}
