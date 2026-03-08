package com.av1.IndicieHashEstatico.models;

import com.av1.IndicieHashEstatico.dto.ResultadoBuscaResponseDTO;
import com.av1.IndicieHashEstatico.dto.ResultadoScanDetalhadoResponseDTO;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Getter
public class IndiceHash {

    private static final int PAGINA_NAO_ENCONTRADA = -1;
    private static final int CUSTO_BUSCA_COM_INDICE = 1;

    private final List<Bucket> buckets;
    private final HashFunction hashFunction;
    private final int totalRegistros;

    public IndiceHash(int numeroBuckets, int capacidadeBucket, int totalRegistros) {
        this.hashFunction = new HashFunction();
        this.totalRegistros = totalRegistros;
        this.buckets = IntStream.range(0, numeroBuckets)
                .mapToObj(i -> new Bucket(i, capacidadeBucket))
                .collect(Collectors.toList());
    }


    public void inserir(String chave, int pagina) {
        localizarBucket(chave).adicionarBucket(chave, pagina);
    }

    public void construirIndice(List<Pagina> paginas) {
        paginas.forEach(pagina ->
                pagina.getPalavras().forEach(palavra ->
                        inserir(palavra, pagina.getNumero())
                )
        );
    }


    public ResultadoBuscaResponseDTO buscar(String chave) {
        long inicioBuscaEmNanos = System.nanoTime();
        Optional<EntradaIndice> entrada = buscarEntrada(chave);
        long tempoDecorrido = System.nanoTime() - inicioBuscaEmNanos;

        return entrada
                .map(e -> new ResultadoBuscaResponseDTO(true, e.getPagina(), CUSTO_BUSCA_COM_INDICE, tempoDecorrido))
                .orElse(new ResultadoBuscaResponseDTO(false, PAGINA_NAO_ENCONTRADA, 0, tempoDecorrido));
    }


    public ResultadoBuscaResponseDTO buscarTableScan(String chave, List<Pagina> paginas) {
        long inicioBuscaEmNanos = System.nanoTime();

        int custo = 0;

        for (Pagina pagina : paginas) {
            custo++;
            for (String palavra : pagina.getPalavras()) {
                if (palavra.equals(chave)) {
                    return new ResultadoBuscaResponseDTO(true, pagina.getNumero(), custo, System.nanoTime() - inicioBuscaEmNanos);
                }
            }
        }

        return new ResultadoBuscaResponseDTO(false, PAGINA_NAO_ENCONTRADA, custo, System.nanoTime() - inicioBuscaEmNanos);
    }

    public ResultadoScanDetalhadoResponseDTO buscarTableScanDetalhado(String chave, List<Pagina> paginas, int limiteRegistros) {
        long inicioBuscaEmNanos = System.nanoTime();

        int custoPaginas = 0;
        List<String> lidos = new ArrayList<>();

        for (Pagina pagina : paginas) {
            custoPaginas++;

            for (String palavra : pagina.getPalavras()) {
                if (limiteRegistros > 0 && lidos.size() >= limiteRegistros) {
                    return montarResultadoDetalhado(false, PAGINA_NAO_ENCONTRADA, custoPaginas, inicioBuscaEmNanos, lidos, true);
                }

                lidos.add(palavra);

                if (palavra.equals(chave)) {
                    return montarResultadoDetalhado(true, pagina.getNumero(), custoPaginas, inicioBuscaEmNanos, lidos, false);
                }
            }
        }

        return montarResultadoDetalhado(false, PAGINA_NAO_ENCONTRADA, custoPaginas, inicioBuscaEmNanos, lidos, false);
    }


    public int getTotalColisoes() {
        return buckets.stream().mapToInt(Bucket::getColisao).sum();
    }

    public double getTaxaColisao() {
        if (totalRegistros == 0) return 0.0;
        return (getTotalColisoes() / (double) totalRegistros) * 100;
    }

    public double getTaxaOverflow() {
        if (totalRegistros == 0 || buckets.isEmpty()) return 0.0;
        long totalOverflows = buckets.stream().mapToLong(Bucket::getQuantidadeOverflows).sum();
        return (totalOverflows / (double) totalRegistros) * 100;
    }


    private Bucket localizarBucket(String chave) {
        int indice = hashFunction.hash(chave, buckets.size());
        return buckets.get(indice);
    }

    private Optional<EntradaIndice> buscarEntrada(String chave) {
        Bucket bucket = localizarBucket(chave);
        return Stream.concat(
                bucket.getBucketPrincipal().stream(),
                bucket.getOverflows().stream()
        ).filter(e -> e.getChave().equals(chave)).findFirst();
    }

    private ResultadoScanDetalhadoResponseDTO montarResultadoDetalhado(
            boolean encontrada, int pagina, int custo,
            long inicioBuscaEmNanos, List<String> lidos, boolean atingiuLimite) {
        return new ResultadoScanDetalhadoResponseDTO(
                encontrada, pagina, custo,
                System.nanoTime() - inicioBuscaEmNanos, lidos, atingiuLimite
        );
    }
}