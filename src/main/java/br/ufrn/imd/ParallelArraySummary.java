package br.ufrn.imd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Responsável pelo carramento e processamento dos itens.
 */
public class ParallelArraySummary {
    public List<Item> carregarItens(int N) {
        List<Item> itens = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 10 * N; i++) {
            itens.add(new Item(i, rand.nextDouble() * 10, rand.nextInt(5) + 1));
        }
        return itens;
    }

    public Resultado processarItens(List<Item> itens, int T) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(T);

        double totalSum = itens.parallelStream().mapToDouble(Item::getTotal).sum();

        Map<Integer, Double> subtotalPorGrupo = itens.parallelStream()
                .collect(Collectors.groupingBy(Item::getGrupo, Collectors.summingDouble(Item::getTotal)));

        long countMenorQue5 = itens.parallelStream().filter(item -> item.getTotal() < 5).count();
        long countMaiorIgual5 = itens.size() - countMenorQue5;

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        return new Resultado(totalSum, subtotalPorGrupo, countMenorQue5, countMaiorIgual5);
    }


    public void exibirResultados(Resultado resultado) {
        System.out.println("Somatório dos Totais: " + resultado.getTotalSum());
        System.out.println("Somatório dos Subtotais por Grupo:");
        resultado.getSubtotalPorGrupo().forEach((grupo, subtotal) -> System.out.println("Grupo " + grupo + ": " + subtotal));
        System.out.println("Número de elementos com total < 5: " + resultado.getCountMenorQue5());
        System.out.println("Número de elementos com total >= 5: " + resultado.getCountMaiorIgual5());
    }
}
