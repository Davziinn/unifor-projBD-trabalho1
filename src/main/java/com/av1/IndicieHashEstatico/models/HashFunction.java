package com.av1.IndicieHashEstatico.models;

public class HashFunction {

    public int hash(String chave, int numeroBuckets) {
        int h = chave.hashCode();
        h = Math.abs(h);
        return h % numeroBuckets;
    }
}
