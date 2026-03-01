package com.av1.IndicieHashEstatico.models;

public class HashFunction {

    public int hash (String chave, int numeroBuckets) {
        int soma = 0;
        for (int i = 0; i < chave.length(); i++) {
            soma += chave.charAt(i);
        }

        return soma % numeroBuckets;
    }
}
