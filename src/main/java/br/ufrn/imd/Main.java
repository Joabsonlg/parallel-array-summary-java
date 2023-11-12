package br.ufrn.imd;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        ParallelArraySummary summary = new ParallelArraySummary();

        System.out.print("Digite o valor de N: ");
        int N = scanner.nextInt();

        System.out.print("Digite o valor de T: ");
        int T = scanner.nextInt();

        List<Item> itens = summary.carregarItens(N);
        System.out.println("Resultados para N=" + N + ", T=" + T + ":");
        long startTime = System.nanoTime();
        Resultado resultado = summary.processarItens(itens, T);
        long endTime = System.nanoTime();
        long tempoExecucao = endTime - startTime;

        System.out.println(summary.formatarResultados(resultado));
        System.out.println("Tempo para N=" + N + ", T=" + T + ": " + tempoExecucao + " nanossegundos.\n");

        String fileName = "resultados/N" + N + "T" + T + ".txt";
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.print("Resultados para N=" + N + ", T=" + T + ":\n");
            writer.print(summary.formatarResultados(resultado));
            writer.print("Tempo para N=" + N + ", T=" + T + ": " + tempoExecucao + " nanossegundos.\n");
        }
    }
}