import javax.swing.*;
import java.awt.*;

public class LakeGUI extends JFrame {
    private Simulation simulation;
    private JPanel gridPanel;

    public LakeGUI(Simulation simulation) {
        this.simulation = simulation;
        Grid grid = simulation.getGrid();

        setTitle("Symulacja Jeziora");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gridPanel = new JPanel(new GridLayout(grid.getHeight(), grid.getWidth()));
        add(gridPanel, BorderLayout.CENTER);

        Timer timer = new Timer(500, e -> {
            simulation.RunTurn();
            updateGrid();
        });
        timer.start();

        updateGrid();
    }

    private void updateGrid() {
        gridPanel.removeAll();
        Grid grid = simulation.getGrid();

        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                Cell cell = grid.getCell(x, y);
                JPanel panel = new JPanel();
                panel.setPreferredSize(new Dimension(20, 20));
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
}
