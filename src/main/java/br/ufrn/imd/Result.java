package br.ufrn.imd;

import java.util.Map;

/**
 * Classe que representa o resultado do processamento dos itens.
 */
public class Result {
    private final double totalSum;
    private final Map<Integer, Double> subtotalByGroup;
    private final long countLessThan5;
    private final long countMajorIgual5;

    public Result(double totalSum, Map<Integer, Double> subtotalByGroup, long countLessThan5, long countMajorIgual5) {
        this.totalSum = totalSum;
        this.subtotalByGroup = subtotalByGroup;
        this.countLessThan5 = countLessThan5;
        this.countMajorIgual5 = countMajorIgual5;
    }

    /**
     * Getters
     */
    public double getTotalSum() {
        return totalSum;
    }

    public Map<Integer, Double> getSubtotalByGroup() {
        return subtotalByGroup;
    }

    public long getCountLessThan5() {
        return countLessThan5;
    }

    public long getCountMajorIgual5() {
        return countMajorIgual5;
    }
}
