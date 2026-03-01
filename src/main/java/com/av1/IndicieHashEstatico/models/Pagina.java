package com.av1.IndicieHashEstatico.models;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pagina {

    private int numero;
    private List<String> palavras;

    public Pagina(int numero) {
        this.numero = numero;
        this.palavras = new ArrayList<>();
    }

    public void addRegistro(String palavra) {
        palavras.add(palavra);
    }

    public int getNumero() {
        return numero;
    }

    public List<String> getPalavras() {
        return palavras;
    }

    public List<String> getPrimeirasPalavras(int quantidade) {
        int limite = Math.min(quantidade, palavras.size());
        return palavras.subList(0, limite);
    }

    public List<Pagina> carregarArquivo(String caminhoArquivo, int tamanhoPagina) throws IOException {
        List<Pagina> paginas = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo));
        String linha;

        int numeroPagina = 0;
        Pagina paginaAtual = new Pagina(numeroPagina);

        while ((linha = reader.readLine()) != null) {
            String[] palavras = linha.split("\\s+");

            for (String palavra : palavras) {
                if (paginaAtual.getPalavras().size() >= tamanhoPagina) {
                    paginas.add(paginaAtual);
                    numeroPagina++;
                    paginaAtual = new Pagina(numeroPagina);
                }

                paginaAtual.addRegistro(palavra);
            }
        }

        paginas.add(paginaAtual);
        reader.close();

        return paginas;
    }
}