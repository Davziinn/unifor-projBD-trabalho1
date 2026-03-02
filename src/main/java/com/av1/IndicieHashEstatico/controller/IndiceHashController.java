package com.av1.IndicieHashEstatico.controller;

import com.av1.IndicieHashEstatico.service.IndiceHashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
