import br.ufrn.imd.Item;
import br.ufrn.imd.ParallelArraySummary;
import br.ufrn.imd.Resultado;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParallelArraySummaryTest {

    @Test
    public void testSomatorioTotais() throws InterruptedException {
        ParallelArraySummary summary = new ParallelArraySummary();
        List<Item> itens = Arrays.asList(
                new Item(1, 4.5, 1),
                new Item(2, 5.5, 2),
                new Item(3, 3.0, 1),
                new Item(4, 6.0, 2)
        );
        Resultado resultado = summary.processarItens(itens, 4);
        Assert.assertEquals(19.0, resultado.getTotalSum(), 0.01);
    }

    @Test
    public void testSomatorioSubtotaisPorGrupo() throws InterruptedException {
        ParallelArraySummary summary = new ParallelArraySummary();
        List<Item> itens = Arrays.asList(
                new Item(1, 4.5, 1),
                new Item(2, 5.5, 2),
                new Item(3, 3.0, 1),
                new Item(4, 6.0, 2)
        );
        Resultado resultado = summary.processarItens(itens, 4);
        Assert.assertEquals(7.5, resultado.getSubtotalPorGrupo().get(1), 0.01);
        Assert.assertEquals(11.5, resultado.getSubtotalPorGrupo().get(2), 0.01);
    }

    @Test
    public void testElementosMenorQue5() throws InterruptedException {
        ParallelArraySummary summary = new ParallelArraySummary();
        List<Item> itens = Arrays.asList(
                new Item(1, 4.5, 1),
                new Item(2, 2.5, 2),
                new Item(3, 3.0, 1),
                new Item(4, 6.0, 2)
        );
        Resultado resultado = summary.processarItens(itens, 4);
        Assert.assertEquals(3, resultado.getCountMenorQue5());
    }

    @Test
    public void testElementosMaiorOuIgual5() throws InterruptedException {
        ParallelArraySummary summary = new ParallelArraySummary();
        List<Item> itens = Arrays.asList(
                new Item(1, 4.5, 1),
                new Item(2, 5.5, 2),
                new Item(3, 3.0, 1),
                new Item(4, 6.0, 2)
        );
        Resultado resultado = summary.processarItens(itens, 4);
        Assert.assertEquals(2, resultado.getCountMaiorIgual5());
    }
}
