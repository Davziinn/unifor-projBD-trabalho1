package com.av1.IndicieHashEstatico.models;

import java.util.ArrayList;
import java.util.List;

public class IndiceHash {

    private List<Bucket> buckets;
    private HashFunction hashFunction;

    public IndiceHash(int numeroBucket, int capacidadeBucket) {
        this.hashFunction = new HashFunction();
        this.buckets = new ArrayList<>();

        for (int i = 0; i < numeroBucket; i++) {
            buckets.add(new Bucket(i, capacidadeBucket));
        }
    }

    public void inserir(String chave, int pagina) {
        int idBucket = hashFunction.hash(chave, buckets.size());
        Bucket bucket = buckets.get(idBucket);

        bucket.adicionar(chave, pagina);
    }

    public int buscar(String chave) {
        int idBucket = hashFunction.hash(chave, buckets.size());
        Bucket bucket = buckets.get(idBucket);

        int custo = 0;

        for (EntradaIndice entradaIndice : bucket.getBucketPrincipal()) {
            custo++;
            if (entradaIndice.getChave().equals(chave)) {
                System.out.println("Custo da busca: " + custo);
                return entradaIndice.getPagina();
            }
        }

        for (EntradaIndice entradaIndice : bucket.getOverflow()) {
            custo++;
            if (entradaIndice.getChave().equals(chave)) {
                System.out.println("Custo da busca: " + custo);
                return entradaIndice.getPagina();
            }
        }

        System.out.println("Custo da busca: " + custo);
        return -1;
    }

    public int getTotalColisoes() {
        int total = 0;
        for (Bucket bucket : buckets) {
            total += bucket.getColisao();
        }

        return total;
    }

    public int buscarTableScan(String chave, List<Pagina> paginas) {
        int custo = 0;
        for (Pagina pagina : paginas) {
            for (String palavra : pagina.getPalavras()) {
                custo++;
                if (palavra.equals(chave)) {
                    System.out.println("Custo da busca (Table Scan): " + custo);
                    return pagina.getNumero();
                }
            }
        }
        System.out.println("Custo da busca (Table Scan): " + custo);
        return -1;
    }

    public int getTotalOverflows() {
        int total = 0;
        for (Bucket bucket : buckets) {
            total += bucket.getContadorOverflows();
        }

        return total;
    }

    public Bucket getBucket(int id) {
        return buckets.get(id);
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void construirIndice (List<Pagina> paginas) {
        for (Pagina pagina : paginas) {
            int numeroPagina = pagina.getNumero();

            for (String palavra : pagina.getPalavras()) {
                inserir(palavra, numeroPagina);
            }
        }
    }
}
