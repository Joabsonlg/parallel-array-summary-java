package br.ufrn.imd;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Responsible for loading and processing items.
 */
public class ParallelArraySummary {
    /**
     * Load the items.
     *
     * @param N Exponent of 10 which defines the number of items to be loaded.
     * @return List of items.
     */
    public List<Item> loadItems(int N) {
        List<Item> items = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < Math.pow(10, N); i++) {
            items.add(new Item(i, rand.nextDouble() * 10, rand.nextInt(5) + 1));
        }
        return items;
    }

    /**
     * Processes the items.
     *
     * @param items List of items to be processed.
     * @param T     Number of threads to be used.
     * @return Processing result.
     * @throws InterruptedException Exception thrown if an error occurs while waiting for the threads to finish.
     */
    public Result processItems(List<Item> items, int T) throws InterruptedException {
        // Estruturas para armazenar resultados intermediários
        // O uso de arrays de tamanho 1 é necessário para que as variáveis possam ser acessadas num lambda
        // sem a necessidade de serem declaradas como final.
        double[] totalSum = new double[1];
        Map<Integer, Double> subtotalByGroup = new HashMap<>();
        long[] countLessThan5 = new long[1];
        long[] countMajorIgual5 = new long[1];

        // Inicializando o ExecutorService. Ele será responsável por gerenciar as threads.
        ExecutorService executor = Executors.newFixedThreadPool(T);

        // Dividindo tarefas entre as threads
        // Cada thread processará um chunk de items. Chunk é uma parte da lista de items.
        int chunkSize = items.size() / T;

        // Para cada thread, é criada uma tarefa que processa um chunk de items.
        for (int i = 0; i < T; i++) {

            // Calculando o índice inicial e final do chunk
            int start = i * chunkSize;
            int end = (i == T - 1) ? items.size() : (start + chunkSize);

            // Criando a tarefa
            executor.submit(() -> {
                for (int j = start; j < end; j++) {
                    Item item = items.get(j);

                    // Sincronização para evitar condições de corrida
                    synchronized (totalSum) {
                        totalSum[0] += item.getTotal();
                    }

                    synchronized (subtotalByGroup) {
                        subtotalByGroup.merge(item.getGroup(), item.getTotal(), Double::sum);
                    }

                    if (item.getTotal() < 5) {
                        synchronized (countLessThan5) {
                            countLessThan5[0]++;
                        }
                    } else {
                        synchronized (countMajorIgual5) {
                            countMajorIgual5[0]++;
                        }
                    }
                }
            });
        }

        // Aguardando a finalização das tarefas
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // Retornando o resultado
        return new Result(totalSum[0], subtotalByGroup, countLessThan5[0], countMajorIgual5[0]);
    }

    /**
     * Generates a string with the results.
     *
     * @param result Result to be formatted.
     * @return A string containing the formatted results.
     */
    public String formatResults(Result result) {
        StringBuilder sb = new StringBuilder();
        sb.append("Somatório dos Totais: ").append(result.getTotalSum()).append("\n");
        sb.append("Somatório dos Subtotais por Grupo:\n");
        for (Map.Entry<Integer, Double> entry : result.getSubtotalByGroup().entrySet()) {
            sb.append("Grupo ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        sb.append("Número de elementos com total < 5: ").append(result.getCountLessThan5()).append("\n");
        sb.append("Número de elementos com total >= 5: ").append(result.getCountMajorIgual5()).append("\n");
        return sb.toString();
    }
}
