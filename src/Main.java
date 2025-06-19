import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataSelector dialog = new DataSelector(null);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                int herbivores = dialog.getHerbivores();
                int carnivores = dialog.getCarnivores();
                int width = dialog.getWidthValue();
                int height = dialog.getHeightValue();

                Simulation simulation = new Simulation(width, height, 100, 0.3); // zakładam, że 0.3 to np. szansa na pojawienie się glonów
                Grid grid = simulation.getGrid();

                for (int i = 0; i < herbivores; i++) {
                    int x = (int)(Math.random() * width);
                    int y = (int)(Math.random() * height);
                    grid.placeFish(new HerbivorousFish(x, y, 100, 500, 100));
                }

                for (int i = 0; i < carnivores; i++) {
                    int x = (int)(Math.random() * width);
                    int y = (int)(Math.random() * height);
                    grid.placeFish(new CarnivorousFish(x, y, 100, 500, 100));
                }

                LakeGUI gui = new LakeGUI(simulation);
                gui.setVisible(true);
            }
        });
    }
}

