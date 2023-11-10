package br.ufrn.imd;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Responsável pelo carramento e processamento dos itens.
 */
public class ParallelArraySummary {
    /**
     * Carrega os itens.
     *
     * @param N Expoente de 10 que define o número de itens a serem carregados.
     * @return Lista de itens.
     */
    public List<Item> carregarItens(int N) {
        List<Item> itens = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 10 * N; i++) {
            itens.add(new Item(i, rand.nextDouble() * 10, rand.nextInt(5) + 1));
        }
        return itens;
    }

    /**
     * Processa os itens.
     *
     * @param itens Lista de itens a serem processados.
     * @param T     Número de threads a serem utilizadas.
     * @return Resultado do processamento.
     * @throws InterruptedException Exceção lançada caso ocorra algum erro ao aguardar a finalização das threads.
     */
    public Resultado processarItens(List<Item> itens, int T) throws InterruptedException {
        // Estruturas para armazenar resultados intermediários
        // O uso de arrays de tamanho 1 é necessário para que as variáveis possam ser acessadas num lambda
        // sem a necessidade de serem declaradas como final.
        double[] totalSum = new double[1];
        Map<Integer, Double> subtotalPorGrupo = new HashMap<>();
        long[] countMenorQue5 = new long[1];
        long[] countMaiorIgual5 = new long[1];

        // Inicializando o ExecutorService. Ele será responsável por gerenciar as threads.
        ExecutorService executor = Executors.newFixedThreadPool(T);

        // Dividindo tarefas entre as threads
        // Cada thread processará um chunk de itens. Chunk é uma parte da lista de itens.
        int chunkSize = itens.size() / T;

        // Para cada thread, é criada uma tarefa que processa um chunk de itens.
        for (int i = 0; i < T; i++) {

            // Calculando o índice inicial e final do chunk
            int start = i * chunkSize;
            int end = (i == T - 1) ? itens.size() : (start + chunkSize);

            // Criando a tarefa
            executor.submit(() -> {
                for (int j = start; j < end; j++) {
                    Item item = itens.get(j);

                    // Sincronização para evitar condições de corrida
                    synchronized (totalSum) {
                        totalSum[0] += item.getTotal();
                    }

                    synchronized (subtotalPorGrupo) {
                        subtotalPorGrupo.merge(item.getGrupo(), item.getTotal(), Double::sum);
                    }

                    if (item.getTotal() < 5) {
                        synchronized (countMenorQue5) {
                            countMenorQue5[0]++;
                        }
                    } else {
                        synchronized (countMaiorIgual5) {
                            countMaiorIgual5[0]++;
                        }
                    }
                }
            });
        }

        // Aguardando a finalização das tarefas
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // Retornando o resultado
        return new Resultado(totalSum[0], subtotalPorGrupo, countMenorQue5[0], countMaiorIgual5[0]);
    }

    /**
     * Exibe os resultados.
     *
     * @param resultado Resultado a ser exibido.
     */
    public void exibirResultados(Resultado resultado) {
        System.out.println("Somatório dos Totais: " + resultado.getTotalSum());
        System.out.println("Somatório dos Subtotais por Grupo:");
        resultado.getSubtotalPorGrupo().forEach((grupo, subtotal) -> System.out.println("Grupo " + grupo + ": " + subtotal));
        System.out.println("Número de elementos com total < 5: " + resultado.getCountMenorQue5());
        System.out.println("Número de elementos com total >= 5: " + resultado.getCountMaiorIgual5());
    }
}
