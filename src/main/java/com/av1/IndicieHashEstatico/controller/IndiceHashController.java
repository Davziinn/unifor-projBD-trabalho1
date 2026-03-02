package com.av1.IndicieHashEstatico.controller;

import com.av1.IndicieHashEstatico.dto.MetricasResponseDTO;
import com.av1.IndicieHashEstatico.dto.ResultadoBuscaResponseDTO;
import com.av1.IndicieHashEstatico.service.IndiceHashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/indice")
public class IndiceHashController {

    @Autowired
    private IndiceHashService service;

    @PostMapping("/construir")
    public ResponseEntity<String> construirIndice(
                @RequestParam MultipartFile arquivo,
                @RequestParam int tamanhoPagina,
                @RequestParam int numeroBuckets,
                @RequestParam int capacidadeBucket) {
        try {
            service.construirIndice(arquivo, tamanhoPagina, numeroBuckets, capacidadeBucket);
            return ResponseEntity.ok("Índice construído com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao construir índice: " + e.getMessage());
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
}
