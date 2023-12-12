import br.ufrn.imd.ItemDataStore;
import br.ufrn.imd.ParallelArraySummary;
import br.ufrn.imd.Result;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParallelArraySummaryTest {
    @Test
    public void testLoadItemsSizeAndRange() {
        ParallelArraySummary summary = new ParallelArraySummary();
        ItemDataStore items = summary.loadItems(2);
        assertEquals(100, items.size());
        for (int i = 0; i < items.size(); i++) {
            Assert.assertTrue(items.getTotal(i) >= 0 && items.getTotal(i) <= 10);
            Assert.assertTrue(items.getGroup(i) >= 1 && items.getGroup(i) <= 5);
        }
    }

    @Test
    public void testProcessItemsResultCount() throws InterruptedException {
        ParallelArraySummary summary = new ParallelArraySummary();
        ItemDataStore items = summary.loadItems(2);
        Result result = summary.processItems(items, 2);
        assertEquals(100, result.getCountLessThan5() + result.getCountMajorIgual5());
    }

    @Test
    public void testWithDifferentThreads() throws InterruptedException {
        ParallelArraySummary summary = new ParallelArraySummary();
        ItemDataStore items = summary.loadItems(2);
        Result result1 = summary.processItems(items, 1);
        Result result2 = summary.processItems(items, 2);
        assertEquals(result1.getTotalSum(), result2.getTotalSum(), 0.01);
        assertEquals(result1.getCountLessThan5(), result2.getCountLessThan5());
        assertEquals(result1.getCountMajorIgual5(), result2.getCountMajorIgual5());
    }

    @Test
    public void testProcessItemsTotalSum() throws InterruptedException {
        ParallelArraySummary summary = new ParallelArraySummary();
        ItemDataStore items = summary.loadItems(2);
        Result result = summary.processItems(items, 2);
        double expectedTotalSum = 0;
        for (int i = 0; i < items.size(); i++) {
            expectedTotalSum += items.getTotal(i);
        }
        assertEquals(expectedTotalSum, result.getTotalSum(), 0.01);
    }

    @Test
    public void testProcessItemsSubtotalByGroup() throws InterruptedException {
        ParallelArraySummary summary = new ParallelArraySummary();
        ItemDataStore items = summary.loadItems(2);
        Result result = summary.processItems(items, 2);
        double[] expectedSubtotals = new double[5];
        for (int i = 0; i < items.size(); i++) {
            expectedSubtotals[items.getGroup(i) - 1] += items.getTotal(i);
        }
        for (int i = 0; i < 5; i++) {
            assertEquals(expectedSubtotals[i], result.getSubtotalByGroup().get(i + 1), 0.01);
        }
    }

    @Test
    public void testProcessItemsCountLessThan5() throws InterruptedException {
        ParallelArraySummary summary = new ParallelArraySummary();
        ItemDataStore items = summary.loadItems(2);
        Result result = summary.processItems(items, 2);
        long expectedCount = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.getTotal(i) < 5) {
                expectedCount++;
            }
        }
        assertEquals(expectedCount, result.getCountLessThan5());
    }

    @Test
    public void testProcessItemsCountMajorIgual5() throws InterruptedException {
        ParallelArraySummary summary = new ParallelArraySummary();
        ItemDataStore items = summary.loadItems(2);
        Result result = summary.processItems(items, 2);
        long expectedCount = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.getTotal(i) >= 5) {
                expectedCount++;
            }
        }
        assertEquals(expectedCount, result.getCountMajorIgual5());
    }

    @Test
    public void testStressTest() throws InterruptedException {
        ParallelArraySummary summary = new ParallelArraySummary();
        ItemDataStore items = summary.loadItems(7);
        Result result = summary.processItems(items, 64);

        long expectedCountLessThan5 = 0;
        long expectedCountMajorIgual5 = 0;
        double expectedTotalSum = 0;
        double[] expectedSubtotals = new double[5];

        for (int i = 0; i < items.size(); i++) {
            float total = items.getTotal(i);
            int group = items.getGroup(i);

            expectedTotalSum += total;
            expectedSubtotals[group - 1] += total;

            if (total < 5) {
                expectedCountLessThan5++;
            } else {
                expectedCountMajorIgual5++;
            }
        }

        assertEquals(expectedTotalSum, result.getTotalSum(), 0.01);
        assertEquals(expectedCountLessThan5, result.getCountLessThan5());
        assertEquals(expectedCountMajorIgual5, result.getCountMajorIgual5());

        for (int i = 0; i < 5; i++) {
            assertEquals(expectedSubtotals[i], result.getSubtotalByGroup().get(i + 1), 0.01);
        }
    }
}
