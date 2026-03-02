package com.av1.IndicieHashEstatico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricasResponseDTO {
    private double taxaColisao;
    private double taxaOverflow;
}
