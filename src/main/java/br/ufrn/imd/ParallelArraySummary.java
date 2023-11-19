package br.ufrn.imd;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

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
        DoubleAdder totalSum = new DoubleAdder();
        Map<Integer, DoubleAdder> subtotalByGroup = new ConcurrentHashMap<>();
        LongAdder countLessThan5 = new LongAdder();
        LongAdder countMajorIgual5 = new LongAdder();;

        ExecutorService executor = Executors.newFixedThreadPool(T);

        int chunkSize = (int) Math.ceil(items.size() / (double) T);

        for (int i = 0; i < T; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, items.size());

            executor.submit(() -> {
                for (int j = start; j < end; j++) {
                    Item item = items.get(j);
                    totalSum.add(item.getTotal());

                    subtotalByGroup.computeIfAbsent(item.getGroup(), k -> new DoubleAdder()).add(item.getTotal());

                    if (item.getTotal() < 5) {
                        countLessThan5.increment();
                    } else {
                        countMajorIgual5.increment();
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        Map<Integer, Double> finalSubtotals = new HashMap<>();
        subtotalByGroup.forEach((k, v) -> finalSubtotals.put(k, v.sum()));

        return new Result(totalSum.sum(), finalSubtotals, countLessThan5.sum(), countMajorIgual5.sum());
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
