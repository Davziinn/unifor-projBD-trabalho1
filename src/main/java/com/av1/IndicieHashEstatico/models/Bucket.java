package com.av1.IndicieHashEstatico.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Bucket {

    private final int id;
    private final int capacidade;

    private final List<EntradaIndice> bucketPrincipal;
    private final List<EntradaIndice> overflows;

    private int colisao;
    private int quantidadeOverflows;

    public Bucket(int id, int capacidade) {
        this.id = id;
        this.capacidade = capacidade;
        this.bucketPrincipal = new ArrayList<>();
        this.overflows = new ArrayList<>();
        this.colisao = 0;
        this.quantidadeOverflows = 0;
    }
    public void adicionarBucket(String chave, int pagina) {
        if (atualizarSeExistir(chave, pagina)) return;

        if (bucketPrincipalNaoEstaCheia()) {
            bucketPrincipal.add(new EntradaIndice(chave, pagina));
            return;
        }

        adicionarOverflow(chave, pagina);
    }


    private boolean atualizarSeExistir(String chave, int pagina) {
        if (atualizarNaLista(bucketPrincipal, chave, pagina)) return true;
        if (atualizarNaLista(overflows, chave, pagina)) return true;
        return false;
    }

    private boolean atualizarNaLista(List<EntradaIndice> lista, String chave, int pagina) {
        for (EntradaIndice entrada : lista) {
            if (entrada.getChave().equals(chave)) {
                entrada.setPagina(pagina);
                return true;
            }
        }
        return false;
    }

    private boolean bucketPrincipalNaoEstaCheia() {
        return bucketPrincipal.size() < capacidade;
    }

    private void adicionarOverflow(String chave, int pagina) {
        if (colisao == 0) colisao = 1;

        overflows.add(new EntradaIndice(chave, pagina));

        if (overflows.size() == 1 || overflows.size() % capacidade == 0) {
            quantidadeOverflows++;
        }
    }

    public int getTotalEntradas() {
        return bucketPrincipal.size() + overflows.size();
    }
}