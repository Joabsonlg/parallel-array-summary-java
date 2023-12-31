package br.ufrn.imd;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
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
    public ItemDataStore loadItems(int N) {
        int size = (int) Math.pow(10, N);
        ItemDataStore store = new ItemDataStore(size);
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        for (int i = 0; i < size; i++) {
            store.setItem(i, rand.nextFloat() * 10, (byte) (rand.nextInt(5) + 1));
        }
        return store;
    }

    /**
     * Processes the items.
     *
     * @param items List of items to be processed.
     * @param T     Number of threads to be used.
     * @return Processing result.
     * @throws InterruptedException Exception thrown if an error occurs while waiting for the threads to finish.
     */
    public Result processItems(ItemDataStore items, int T) throws InterruptedException {
        DoubleAdder totalSum = new DoubleAdder();
        Map<Integer, DoubleAdder> subtotalByGroup = new ConcurrentHashMap<>();
        LongAdder countLessThan5 = new LongAdder();
        LongAdder countMajorIgual5 = new LongAdder();

        ExecutorService executor = Executors.newFixedThreadPool(T);
        int chunkSize = (int) Math.ceil(items.size() / (double) T);

        for (int i = 0; i < T; i++) {
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, items.size());

            executor.submit(() -> {
                for (int j = start; j < end; j++) {
                    float total = items.getTotal(j);
                    int group = items.getGroup(j);

                    totalSum.add(total);
                    subtotalByGroup.computeIfAbsent(group, k -> new DoubleAdder()).add(total);

                    if (total < 5) {
                        countLessThan5.increment();
                    } else {
                        countMajorIgual5.increment();
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);

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
