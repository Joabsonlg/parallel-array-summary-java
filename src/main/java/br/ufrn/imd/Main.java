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

        List<Item> items = summary.loadItems(N);
        long startTime = System.nanoTime();
        Result result = summary.processItems(items, T);
        long endTime = System.nanoTime();
        long timeExecution = endTime - startTime;

        System.out.println("Resultados para N=" + N + ", T=" + T + ":");
        System.out.println(summary.formatResults(result));
        System.out.println("Tempo para N=" + N + ", T=" + T + ": " + timeExecution + " nanossegundos.\n");

        String fileName = "resultados/N" + N + "T" + T + ".txt";
        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.print("Resultados para N=" + N + ", T=" + T + ":\n");
            writer.print(summary.formatResults(result));
            writer.print("Tempo para N=" + N + ", T=" + T + ": " + timeExecution + " nanossegundos.\n");
        }
    }
}