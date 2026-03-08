package com.av1.IndicieHashEstatico.models;

import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Data
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

    public List<String> getPrimeirasPalavras(int quantidade) {
        int limite = Math.min(quantidade, palavras.size());
        return palavras.subList(0, limite);
    }

    public List<Pagina> carregarArquivo(InputStream inputStream, int tamanhoPagina) throws IOException {
        List<Pagina> paginas = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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
        return paginas;
    }
}