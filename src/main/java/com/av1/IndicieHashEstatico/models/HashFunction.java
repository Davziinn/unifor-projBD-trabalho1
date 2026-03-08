package com.av1.IndicieHashEstatico.models;

public class HashFunction {

    public int hash(String chave, int numeroBuckets) {
        return Math.abs(chave.hashCode()) % numeroBuckets;
    }
}