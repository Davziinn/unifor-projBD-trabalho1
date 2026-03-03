package com.av1.IndicieHashEstatico.models;

import com.av1.IndicieHashEstatico.dto.ResultadoBuscaResponseDTO;
import com.av1.IndicieHashEstatico.dto.ResultadoScanDetalhadoResponseDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class IndiceHash {

    private List<Bucket> buckets;
    private HashFunction hashFunction;
    private int totalRegistrosInseridos = 0;

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
        totalRegistrosInseridos++;
    }

    public ResultadoBuscaResponseDTO buscar(String chave) {
        long inicio = System.nanoTime();

        boolean encontrada = false;
        int pagina = -1;

        int idBucket = hashFunction.hash(chave, buckets.size());
        Bucket bucket = buckets.get(idBucket);

        for (EntradaIndice entradaIndice : bucket.getBucketPrincipal()) {
            if (entradaIndice.getChave().equals(chave)) {
                encontrada = true;
                pagina = entradaIndice.getPagina();
                break;
            }
        }

        if (!encontrada) {
            for (EntradaIndice entradaIndice : bucket.getOverflows()) {
                if (entradaIndice.getChave().equals(chave)) {
                    encontrada = true;
                    pagina = entradaIndice.getPagina();
                    break;
                }
            }
        }

        int custo = encontrada ? 1 : 0;

        long fim = System.nanoTime();
        long tempo = fim - inicio;

        return new ResultadoBuscaResponseDTO(encontrada, pagina, custo, tempo);
    }

    public int getTotalColisoes() {
        int total = 0;
        for (Bucket bucket : buckets) {
            total += bucket.getColisao();
        }

        return total;
    }

    public ResultadoBuscaResponseDTO buscarTableScan(String chave, List<Pagina> paginas) {

        long inicio = System.nanoTime();

        int custo = 0;
        boolean encontrada = false;
        int paginaEncontrada = -1;

        for (Pagina pagina : paginas) {
            custo++;

            for (String palavra : pagina.getPalavras()) {
                if (palavra.equals(chave)) {
                    encontrada = true;
                    paginaEncontrada = pagina.getNumero();

                    long fim = System.nanoTime();
                    long tempo = fim - inicio;

                    return new ResultadoBuscaResponseDTO(encontrada, paginaEncontrada, custo, tempo);
                }
            }
        }

        long fim = System.nanoTime();
        long tempo = fim - inicio;

        return new ResultadoBuscaResponseDTO(encontrada, paginaEncontrada, custo, tempo);
    }

    public Bucket getBucket(int id) {
        return buckets.get(id);
    }

    public void construirIndice(List<Pagina> paginas) {
        for (Pagina pagina : paginas) {
            int numeroPagina = pagina.getNumero();

            for (String palavra : pagina.getPalavras()) {
                inserir(palavra, numeroPagina);
            }
        }
    }

    public double getTaxaColisao() {
        if (totalRegistrosInseridos == 0) {
            return 0;
        }
        return (getTotalColisoes() / (double) totalRegistrosInseridos) * 100;
    }

    public double getTaxaOverflow() {
        int bucketEmOverflow = 0;

        for (Bucket bucket : buckets) {
            if (bucket.isOverflow()) {
                bucketEmOverflow++;
            }
        }

        return (bucketEmOverflow / (double) buckets.size()) * 100;
    }

    public ResultadoScanDetalhadoResponseDTO buscarTableScanDetalhado(String chave, List<Pagina> paginas, int limiteRegistros) {

        long inicio = System.nanoTime();

        int custoPaginas = 0;
        boolean encontrada = false;
        int paginaEncontrada = -1;

        List<String> lidos = new ArrayList<>();
        boolean atingiuLimite = false;

        for (Pagina pagina : paginas) {
            custoPaginas++;

            for (String palavra : pagina.getPalavras()) {

                if (limiteRegistros > 0 && lidos.size() >= limiteRegistros) {
                    atingiuLimite = true;
                    break;
                }

                lidos.add(palavra);

                if (palavra.equals(chave)) {
                    encontrada = true;
                    paginaEncontrada = pagina.getNumero();
                    long fim = System.nanoTime();
                    return new ResultadoScanDetalhadoResponseDTO(encontrada, paginaEncontrada, custoPaginas,
                            fim - inicio, lidos, atingiuLimite);
                }
            }

            if (atingiuLimite) break;
        }

        long fim = System.nanoTime();
        return new ResultadoScanDetalhadoResponseDTO(encontrada, paginaEncontrada, custoPaginas,
                fim - inicio, lidos, atingiuLimite);
    }
}
