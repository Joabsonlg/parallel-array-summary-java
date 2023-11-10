package br.ufrn.imd;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ParallelArraySummary summary = new ParallelArraySummary();
        int[] valoresN = {5, 7, 9};
        int[] valoresT = {1, 4, 16, 64, 256};

        for (int N : valoresN) {
            List<Item> itens = summary.carregarItens(N);
            for (int T : valoresT) {
                System.out.println("Resultados para N=" + N + ", T=" + T + ":");
                long startTime = System.nanoTime();
                Resultado resultado = summary.processarItens(itens, T);
                summary.exibirResultados(resultado);
                long endTime = System.nanoTime();
                System.out.println("Tempo para N=" + N + ", T=" + T + ": " + (endTime - startTime) + " nanossegundos.\n");
            }
            System.out.println("__________________________________________________");
        }
    }
}