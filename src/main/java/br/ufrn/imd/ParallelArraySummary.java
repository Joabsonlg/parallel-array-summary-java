package br.ufrn.imd;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * Responsible for loading and processing items.
 */
public class ParallelArraySummary {
    /**
     * Load the items.
     *
     * @param N Exponent of 10 which defines the number of items to be loaded.
     */
    public void loadItems(int N) {
        System.out.println("Gerando itens para N=" + N + "...");
        Random rand = new Random();
        int numItems = (int) Math.pow(10, N);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("items.txt"))) {
            for (int i = 0; i < numItems; i++) {
                double total = rand.nextDouble() * 10;
                int group = rand.nextInt(5) + 1;
                writer.write(i + "," + total + "," + group + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Itens gerados.\n");
    }

    private static final int BATCH_SIZE = 100000;

    /**
     * Processes the items in the file 'items.txt' using a custom thread pool.
     * The number of threads in the pool is defined by the parameter T.
     * The method reads the file in batches, processes each batch in parallel, and accumulates the results.
     * The results include the total sum of all items, subtotal by group, count of items less than 5, and count of items greater or equal to 5.
     *
     * @param T The number of threads to be used in the custom thread pool.
     * @return A Result object containing the total sum, subtotal by group, count of items less than 5, and count of items greater or equal to 5.
     */
    public Result processItems(int T) {
        System.out.println("Processando itens com T=" + T + "...");
        ForkJoinPool customThreadPool = new ForkJoinPool(T);
        try {
            DoubleAdder totalSum = new DoubleAdder();
            ConcurrentHashMap<Integer, Double> subtotalByGroup = new ConcurrentHashMap<>();
            LongAdder countLessThan5 = new LongAdder();
            LongAdder countMajorIgual5 = new LongAdder();

            try (BufferedReader reader = new BufferedReader(new FileReader("items.txt"))) {
                List<String> batch;
                while (!(batch = reader.lines().limit(BATCH_SIZE).collect(Collectors.toList())).isEmpty()) {
                    processBatch(batch, customThreadPool, totalSum, subtotalByGroup, countLessThan5, countMajorIgual5);
                }
            }

            return new Result(totalSum.sum(), subtotalByGroup, countLessThan5.sum(), countMajorIgual5.sum());
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            customThreadPool.shutdown();
            System.out.println("Processamento concluído.\n");
        }
    }

    /**
     * Processes a batch of items in parallel using a custom thread pool.
     * Each item is split into parts, the total and group are extracted, and the results are accumulated.
     *
     * @param batch The batch of items to be processed.
     * @param customThreadPool The custom thread pool to be used for parallel processing.
     * @param totalSum The DoubleAdder to accumulate the total sum of all items.
     * @param subtotalByGroup The ConcurrentHashMap to accumulate the subtotal by group.
     * @param countLessThan5 The LongAdder to count the number of items less than 5.
     * @param countMajorIgual5 The LongAdder to count the number of items greater or equal to 5.
     * @throws InterruptedException If the current thread was interrupted while waiting.
     * @throws ExecutionException If the computation threw an exception.
     */
    private void processBatch(List<String> batch, ForkJoinPool customThreadPool, DoubleAdder totalSum, ConcurrentHashMap<Integer,
            Double> subtotalByGroup, LongAdder countLessThan5, LongAdder countMajorIgual5) throws InterruptedException, ExecutionException {
        customThreadPool.submit(() ->
                batch.forEach(line -> {
                    String[] parts = line.split(",");
                    double total = Double.parseDouble(parts[1]);
                    int group = Integer.parseInt(parts[2]);

                    totalSum.add(total);

                    subtotalByGroup.compute(group, (key, val) -> (val == null) ? total : val + total);
                    if (total < 5) {
                        countLessThan5.increment();
                    } else {
                        countMajorIgual5.increment();
                    }
                })
        ).get();
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
