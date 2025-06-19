import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class LakeGUI extends JFrame {
    private final Simulation simulation;
    private final JPanel gridPanel;
    private final JLabel herbivoresLabel;
    private final JLabel carnivoresLabel;
    private final JLabel temperatureLabel;
    private final JLabel pollutionLabel;
    private final JLabel seasonLabel;
    private final JLabel eventLabel;
    private final JLabel historyLabel;
    private DefaultCategoryDataset dataset;
    private ChartPanel chartPanel;

    public LakeGUI(Simulation simulation) {
        this.simulation = simulation;
        Grid grid = simulation.getGrid();

        try (PrintWriter writer = new PrintWriter("fish_log.txt")) {
            writer.print("");
        } catch (IOException e) {
            System.out.println("Błąd czyszczenia fish_log.txt: " + e.getMessage());
        }

        setTitle("Symulacja Jeziora");
        setSize(1200, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel siatki (gridu)
        gridPanel = new JPanel(new GridLayout(grid.getHeight(), grid.getWidth()));

        // Panel informacyjny
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informacje"));
        infoPanel.setPreferredSize(new Dimension(240, getHeight()));

        herbivoresLabel = new JLabel("Roślinożerne: 0");
        herbivoresLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        carnivoresLabel = new JLabel("Mięsożerne: 0");
        carnivoresLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        temperatureLabel = new JLabel("Temperatura: 0°C");
        temperatureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        pollutionLabel = new JLabel("Zanieczyszczenie: 0%");
        pollutionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        seasonLabel = new JLabel("Pora roku: Wiosna");
        seasonLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        eventLabel = new JLabel("<html><div style='text-align:center;'>Zdarzenia: brak</div></html>");
        eventLabel.setMaximumSize(new Dimension(160, 100));
        eventLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        historyLabel = new JLabel("<html>Historia: brak</html>");
        historyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Wykres
        dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createLineChart(
                "Populacja ryb",
                "Tura",
                "Liczba",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(220, 200));
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickUnit(new NumberTickUnit(1));

        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(eventLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(herbivoresLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(carnivoresLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(temperatureLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(pollutionLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(seasonLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(historyLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(chartPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gridPanel, infoPanel);
        splitPane.setResizeWeight(0.8);
        splitPane.setEnabled(false);
        add(splitPane, BorderLayout.CENTER);

        JButton stopSimulation = new JButton("Stop");
        stopSimulation.addActionListener(e -> simulation.stopSimulation());
        add(stopSimulation, BorderLayout.SOUTH);

        Timer timer = new Timer(1000, e -> {
            simulation.RunTurn();
            updateGrid();
            updateInfoPanel();
        });
        timer.start();

        updateGrid();
        updateInfoPanel();
    }

    private void updateGrid() {
        gridPanel.removeAll();
        Grid grid = simulation.getGrid();

        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                Cell cell = grid.getCell(x, y);
                JPanel panel = new JPanel();
                panel.setPreferredSize(new Dimension(12, 12));
                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                if (!cell.isEmpty()) {
                    Fish fish = cell.getFish();
                    if (fish instanceof CarnivorousFish) {
                        panel.setBackground(Color.RED);
                    } else if (fish instanceof HerbivorousFish) {
                        panel.setBackground(Color.BLUE);
                    }
                } else if (cell.getPlankton() != null && !cell.getPlankton().isDepleted()) {
                    panel.setBackground(Color.GREEN);
                } else {
                    panel.setBackground(Color.WHITE);
                }

                gridPanel.add(panel);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void updateInfoPanel() {
        Grid grid = simulation.getGrid();
        int herbivores = 0;
        int carnivores = 0;

        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                Fish fish = grid.getCell(x, y).getFish();
                if (fish instanceof HerbivorousFish) herbivores++;
                else if (fish instanceof CarnivorousFish) carnivores++;
            }
        }

        herbivoresLabel.setText("Roślinożerne: " + herbivores);
        carnivoresLabel.setText("Mięsożerne: " + carnivores);
        temperatureLabel.setText("Temperatura: " + simulation.getTemperature() + "°C");
        pollutionLabel.setText("Zanieczyszczenie: " + simulation.getPollutionLevel() + "%");
        seasonLabel.setText("Pora roku: " + simulation.getSeason());
        eventLabel.setText("<html><div style='text-align:center;'>Zdarzenia: " + simulation.getLastEventMessage() + "</div></html>");

        try {
            List<String> lines = Files.readAllLines(Paths.get("fish_log.txt"));
            dataset.clear();

            StringBuilder sb = new StringBuilder();

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String round = parts[0];
                    int herb = Integer.parseInt(parts[1]);
                    int carn = Integer.parseInt(parts[2]);
                    dataset.addValue(carn, "Mięsożerne", round);
                    dataset.addValue(herb, "Roślinożerne", round);
                }
            }
            historyLabel.setText(sb.toString());

        } catch (IOException e) {
            historyLabel.setText("<html>Błąd odczytu danych</html>");
        }
    }
}
