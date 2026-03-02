package com.av1.IndicieHashEstatico.service;

import com.av1.IndicieHashEstatico.dto.MetricasResponseDTO;
import com.av1.IndicieHashEstatico.models.*;
import com.av1.IndicieHashEstatico.dto.ResultadoBuscaResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class IndiceHashService {

    private IndiceHash indiceHash;
    private List<Pagina> paginas;

    public void construirIndice(MultipartFile arquivo,
                                int tamanhoPagina,
                                int numeroBuckets,
                                int capacidadeBucket) throws IOException {

        Pagina loader = new Pagina(0);
        this.paginas = loader.carregarArquivo(arquivo.getInputStream(), tamanhoPagina);

        this.indiceHash = new IndiceHash(numeroBuckets, capacidadeBucket);
        this.indiceHash.construirIndice(paginas);
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
}