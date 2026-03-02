package com.av1.IndicieHashEstatico.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Bucket {

    private int id;
    private int capacidade;

    private List<EntradaIndice> bucketPrincipal;
    private List<EntradaIndice> overflows;
    private int colisao;
    private boolean overflow;

    public Bucket(int id, int capacidade) {
        this.id = id;
        this.capacidade = capacidade;
        this.bucketPrincipal = new ArrayList<>();
        this.overflows = new ArrayList<>();
        this.colisao = 0;
        this.overflow = false;
    }

    public void adicionar (String chave, int pagina) {

        for (EntradaIndice entradaIndice : bucketPrincipal) {
            if (entradaIndice.getChave().equals(chave)) {
                entradaIndice.setPagina(pagina);
                return;
            }
        }

        for (EntradaIndice entradaIndice : overflows) {
            if (entradaIndice.getChave().equals(chave)) {
                entradaIndice.setPagina(pagina);
                return;
            }
        }

        if (!bucketPrincipal.isEmpty()) {
            colisao++;
        }

        if (bucketPrincipal.size() < capacidade) {
            bucketPrincipal.add(new EntradaIndice(chave, pagina));
        } else {
            overflows.add(new EntradaIndice(chave, pagina));
            if (!overflow) {
                overflow = true;
            }
        }
    }

    public boolean isCheio () {
        return bucketPrincipal.size() >= capacidade;
    }

}
