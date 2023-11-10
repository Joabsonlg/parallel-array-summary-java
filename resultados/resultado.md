# Relatório de Resultados do Parallel Array Summary

## Metodologia
Foram realizados testes com três valores de N (5, 7, 9) e cinco quantidades de threads (1, 4, 16, 64, 256). As operações realizadas incluem o cálculo do somatório dos totais, somatório dos subtotais por grupo, e contagem de elementos com total menor que 5 e maior ou igual a 5.

## Resultados

### Para N=5
- **T=1:** Tempo de 22.296.970 ns
- **T=4:** Tempo de 2.857.836 ns
- **T=16:** Tempo de 5.362.623 ns
- **T=64:** Tempo de 27.921.359 ns
- **T=256:** Tempo de 49.534.862 ns

### Para N=7
- **T=1:** Tempo de 8.834.433 ns
- **T=4:** Tempo de 3.581.391 ns
- **T=16:** Tempo de 3.933.880 ns
- **T=64:** Tempo de 10.875.825 ns
- **T=256:** Tempo de 42.872.912 ns

### Para N=9
- **T=1:** Tempo de 2.896.318 ns
- **T=4:** Tempo de 10.105.615 ns
- **T=16:** Tempo de 2.689.937 ns
- **T=64:** Tempo de 9.377.171 ns
- **T=256:** Tempo de 45.276.639 ns

## Análise
Os resultados indicam uma relação complexa entre o número de threads e o tempo de processamento. Em alguns casos, 
um maior número de threads reduz o tempo de processamento, mas essa relação não é linear nem consistente em todas as 
configurações. Observa-se que, para um valor fixo de N, o aumento de threads nem sempre corresponde a uma diminuição 
proporcional do tempo, evidenciando o overhead associado à gestão de múltiplas threads.
