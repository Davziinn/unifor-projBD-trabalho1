package com.av1.IndicieHashEstatico.models;

import java.util.ArrayList;
import java.util.List;

public class Bucket {

    private int id;
    private int capacidade;

    private List<EntradaIndice> bucketPrincipal;
    private List<EntradaIndice> overflow;
    private int colisao;
    private int contadorOverflows;

    public Bucket(int id, int capacidade) {
        this.id = id;
        this.capacidade = capacidade;
        this.bucketPrincipal = new ArrayList<>();
        this.overflow = new ArrayList<>();
        this.colisao = 0;
        this.contadorOverflows = 0;
    }

    public void adicionar (String chave, int pagina) {

        for (EntradaIndice entradaIndice : bucketPrincipal) {
            if (entradaIndice.getChave().equals(chave)) {
                entradaIndice.setPagina(pagina);
                return;
            }
        }

        for (EntradaIndice entradaIndice : overflow) {
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
            contadorOverflows++;
            overflow.add(new EntradaIndice(chave, pagina));
        }
    }

    public boolean isCheio () {
        return bucketPrincipal.size() >= capacidade;
    }

    public List<EntradaIndice> getBucketPrincipal() {
        return bucketPrincipal;
    }

    public List<EntradaIndice> getOverflow() {
        return overflow;
    }

    public int getColisao() {
        return colisao;
    }

    public int getContadorOverflows() {
        return contadorOverflows;
    }

    public int getId() {
        return id;
    }
}
