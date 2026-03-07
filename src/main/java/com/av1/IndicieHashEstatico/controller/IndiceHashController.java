package com.av1.IndicieHashEstatico.controller;

import com.av1.IndicieHashEstatico.dto.*;
import com.av1.IndicieHashEstatico.service.IndiceHashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/indice")
@CrossOrigin(origins = "*")
public class IndiceHashController {

    @Autowired
    private IndiceHashService service;

    @PostMapping("/construir")
    public ResponseEntity<?> construirIndice(
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("tamanhoPagina") int tamanhoPagina,
            @RequestParam("capacidadeBucket") int capacidadeBucket) {
        try {
            ConstruirIndiceResponseDTO resp =
                    service.construirIndice(arquivo, tamanhoPagina, capacidadeBucket);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/busca/hash")
    public ResponseEntity<ResultadoBuscaResponseDTO> buscarPeloHash (@RequestParam String palavra) {
        try {
            ResultadoBuscaResponseDTO palavraBuscada = service.buscarHash(palavra);
            return ResponseEntity.ok().body(palavraBuscada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/busca/scan")
    public ResponseEntity<ResultadoBuscaResponseDTO> buscarPeloScan (@RequestParam String palavra) {
        try {
            ResultadoBuscaResponseDTO palavraBuscada = service.buscarScan(palavra);
            return ResponseEntity.ok().body(palavraBuscada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/metricas")
    public ResponseEntity<MetricasResponseDTO> buscarMetricas() {
        try {
            MetricasResponseDTO metricas = service.getMetricas();
            return ResponseEntity.ok(metricas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/paginas/resumo")
    public ResponseEntity<?> resumoPaginas() {
        try {
            ResumoPaginasResponseDTO resp = service.getResumoPaginas();
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/busca/scan-detalhado")
    public ResponseEntity<?> buscarPeloScanDetalhado(
            @RequestParam String palavra,
            @RequestParam(required = false, defaultValue = "200") int limiteRegistros) {
        try {
            ResultadoScanDetalhadoResponseDTO resp = service.buscarScanDetalhado(palavra, limiteRegistros);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
