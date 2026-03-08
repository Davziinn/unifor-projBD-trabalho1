package com.av1.IndicieHashEstatico.service;

import com.av1.IndicieHashEstatico.dto.*;
import com.av1.IndicieHashEstatico.models.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class IndiceHashService {

    private static final int LIMITE_SCAN_PADRAO = 200;
    private static final String ERRO_INDICE_NAO_CONSTRUIDO = "Índice ainda não foi construído";

    private IndiceHash indiceHash;
    private List<Pagina> paginas;

    public ConstruirIndiceResponseDTO construirIndice(
            MultipartFile arquivo,
            int tamanhoPagina,
            int capacidadeBucket) throws IOException {

        long inicioConstrucaoEmNanos = System.nanoTime();

        this.paginas = carregarPaginas(arquivo, tamanhoPagina);

        int totalRegistros = contarRegistros(paginas);
        int numeroBuckets = calcularNumeroBuckets(totalRegistros, capacidadeBucket);

        this.indiceHash = new IndiceHash(numeroBuckets, capacidadeBucket, totalRegistros);
        this.indiceHash.construirIndice(paginas);

        long tempoDecorrido = System.nanoTime() - inicioConstrucaoEmNanos;

        return new ConstruirIndiceResponseDTO(
                totalRegistros,
                paginas.size(),
                numeroBuckets,
                capacidadeBucket,
                tempoDecorrido
        );
    }

    public ResultadoBuscaResponseDTO buscarHash(String palavra) {
        validarIndice();
        return indiceHash.buscar(palavra);
    }

    public ResultadoBuscaResponseDTO buscarScan(String palavra) {
        validarIndice();
        return indiceHash.buscarTableScan(palavra, paginas);
    }

    public MetricasResponseDTO getMetricas() {
        validarIndice();
        return new MetricasResponseDTO(
                indiceHash.getTaxaColisao(),
                indiceHash.getTaxaOverflow()
        );
    }

    public ResumoPaginasResponseDTO getResumoPaginas() {
        validarPaginas();

        Pagina primeira = paginas.get(0);
        Pagina ultima = paginas.get(paginas.size() - 1);

        return new ResumoPaginasResponseDTO(
                paginas.size(),
                toPaginaResumoDTO(primeira),
                toPaginaResumoDTO(ultima)
        );
    }

    public ResultadoScanDetalhadoResponseDTO buscarScanDetalhado(String palavra, int limiteRegistros) {
        validarIndice();
        validarPaginas();

        int limite = limiteRegistros <= 0 ? LIMITE_SCAN_PADRAO : limiteRegistros;
        return indiceHash.buscarTableScanDetalhado(palavra, paginas, limite);
    }


    private List<Pagina> carregarPaginas(MultipartFile arquivo, int tamanhoPagina) throws IOException {
        return new Pagina(0).carregarArquivo(arquivo.getInputStream(), tamanhoPagina);
    }

    private int contarRegistros(List<Pagina> paginas) {
        return paginas.stream()
                .mapToInt(p -> p.getPalavras().size())
                .sum();
    }

    private int calcularNumeroBuckets(int totalRegistros, int capacidadeBucket) {
        double minimo = (double) totalRegistros / capacidadeBucket;
        return (int) Math.ceil(minimo) + 1;
    }

    private PaginaResumoDTO toPaginaResumoDTO(Pagina pagina) {
        return new PaginaResumoDTO(pagina.getNumero(), pagina.getPrimeirasPalavras(5));
    }

    private void validarIndice() {
        if (indiceHash == null) {
            throw new IllegalStateException(ERRO_INDICE_NAO_CONSTRUIDO);
        }
    }

    private void validarPaginas() {
        if (paginas == null || paginas.isEmpty()) {
            throw new IllegalStateException("Páginas ainda não foram carregadas. Construa o índice primeiro.");
        }
    }
}