package br.ufrn.imd;

import java.util.Map;

/**
 * Classe que representa o resultado do processamento dos itens.
 */
public class Resultado {
    private final double totalSum;
    private final Map<Integer, Double> subtotalPorGrupo;
    private final long countMenorQue5;
    private final long countMaiorIgual5;

    public Resultado(double totalSum, Map<Integer, Double> subtotalPorGrupo, long countMenorQue5, long countMaiorIgual5) {
        this.totalSum = totalSum;
        this.subtotalPorGrupo = subtotalPorGrupo;
        this.countMenorQue5 = countMenorQue5;
        this.countMaiorIgual5 = countMaiorIgual5;
    }

    /**
     * Getters
     */
    public double getTotalSum() {
        return totalSum;
    }

    public Map<Integer, Double> getSubtotalPorGrupo() {
        return subtotalPorGrupo;
    }

    public long getCountMenorQue5() {
        return countMenorQue5;
    }

    public long getCountMaiorIgual5() {
        return countMaiorIgual5;
    }
}
