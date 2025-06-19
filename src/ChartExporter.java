import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChartExporter {
    private DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    public void addDataPoint(int turn, int herbivores, int carnivores) {
        dataset.addValue(carnivores, "Mięsożerne", String.valueOf(turn));
        dataset.addValue(herbivores, "Roślinożerne", String.valueOf(turn));
    }

    public void exportChart(String filePath) {
        JFreeChart chart = ChartFactory.createLineChart(
                "Populacja ryb w czasie",
                "Tura",
                "Liczba ryb",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickUnit(new NumberTickUnit(1));

        try {
            ChartUtilities.saveChartAsPNG(new File(filePath), chart, 800, 600);
            System.out.println("Wykres zapisany do: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

