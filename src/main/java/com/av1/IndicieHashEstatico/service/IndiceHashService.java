package com.av1.IndicieHashEstatico.service;

import com.av1.IndicieHashEstatico.dto.*;
import com.av1.IndicieHashEstatico.models.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class IndiceHashService {

    private IndiceHash indiceHash;
    private List<Pagina> paginas;

    public ConstruirIndiceResponseDTO construirIndice(MultipartFile arquivo,
                                                      int tamanhoPagina,
                                                      int numeroBuckets,
                                                      int capacidadeBucket) throws IOException {

        Pagina loader = new Pagina(0);
        this.paginas = loader.carregarArquivo(arquivo.getInputStream(), tamanhoPagina);

        int nr = 0;
        for (Pagina p : paginas) {
            nr += p.getPalavras().size();
        }

        if ((long) numeroBuckets * (long) capacidadeBucket <= (long) nr) {
            throw new IllegalArgumentException(
                    "Configuração inválida: numeroBuckets * capacidadeBucket deve ser > totalRegistros (NR). " +
                            "NR=" + nr + ", NB=" + numeroBuckets + ", FR=" + capacidadeBucket
            );
        }

        this.indiceHash = new IndiceHash(numeroBuckets, capacidadeBucket);

        long inicioConstrucao = System.nanoTime();
        this.indiceHash.construirIndice(paginas);
        long fimConstrucao = System.nanoTime();

        long tempoConstrucao = fimConstrucao - inicioConstrucao;

        return new ConstruirIndiceResponseDTO(nr, paginas.size(), tempoConstrucao);
    }

    public ResultadoBuscaResponseDTO buscarHash(String palavra) {
        if (indiceHash == null) {
            throw new RuntimeException("Índice ainda não foi construído");
        }
        return indiceHash.buscar(palavra);
    }

    public ResultadoBuscaResponseDTO buscarScan(String palavra) {
        if (indiceHash == null) {
            throw new RuntimeException("Índice ainda não foi construído");
        }
        return indiceHash.buscarTableScan(palavra, paginas); // depois vamos refatorar para DTO
    }

    public MetricasResponseDTO getMetricas() {
        if (indiceHash == null) {
            throw new IllegalStateException("Índice ainda não foi construído");
        }
        return new MetricasResponseDTO(
                indiceHash.getTaxaColisao(),
                indiceHash.getTaxaOverflow()
        );
    }

    public ResumoPaginasResponseDTO getResumoPaginas() {
        if (paginas == null || paginas.isEmpty()) {
            throw new IllegalStateException("Páginas ainda não foram carregadas. Construa o índice primeiro.");
        }

        Pagina primeira = paginas.get(0);
        Pagina ultima = paginas.get(paginas.size() - 1);

        PaginaResumoDTO primeiraDTO = new PaginaResumoDTO(
                primeira.getNumero(),
                primeira.getPrimeirasPalavras(5)
        );

        PaginaResumoDTO ultimaDTO = new PaginaResumoDTO(
                ultima.getNumero(),
                ultima.getPrimeirasPalavras(5)
        );

        return new ResumoPaginasResponseDTO(paginas.size(), primeiraDTO, ultimaDTO);
    }

    public ResultadoScanDetalhadoResponseDTO buscarScanDetalhado(String palavra, int limiteRegistros) {
        if (indiceHash == null || paginas == null) {
            throw new IllegalStateException("Índice ainda não foi construído");
        }
        int limite = limiteRegistros <= 0 ? 200 : limiteRegistros;
        return indiceHash.buscarTableScanDetalhado(palavra, paginas, limite);
    }
}