import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DataSelector extends JDialog {

    private final JSlider herbivoresSlider = new JSlider(0, 20, 10);
    private final JSlider carnivoresSlider = new JSlider(0, 20, 5);
    private final JSlider widthSlider = new JSlider(5, 50, 30);
    private final JSlider heightSlider = new JSlider(5, 50, 30);

    private boolean confirmed = false;

    public DataSelector(Frame parent) {
        super(parent, "Ustawienia symulacji", true);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Liczba roślinożernych:"));
        add(setupSlider(herbivoresSlider));

        add(new JLabel("Liczba mięsożernych:"));
        add(setupSlider(carnivoresSlider));

        add(new JLabel("Szerokość planszy:"));
        add(setupSlider(widthSlider));

        add(new JLabel("Wysokość planszy:"));
        add(setupSlider(heightSlider));


        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JButton startButton = new JButton("Start");
        startButton.addActionListener(this::onStartClicked);
        add(startButton);
        add(new JLabel());
        pack();

        setLocationRelativeTo(parent);
    }

    private JPanel setupSlider(JSlider slider) {
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing((slider.getMaximum() - slider.getMinimum()) / 4);
        slider.setMinorTickSpacing(1);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(slider, BorderLayout.CENTER);
        return panel;
    }

    private void onStartClicked(ActionEvent e) {
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public int getHerbivores() {
        return herbivoresSlider.getValue();
    }

    public int getCarnivores() {
        return carnivoresSlider.getValue();
    }

    public int getWidthValue() {
        return widthSlider.getValue();
    }

    public int getHeightValue() {
        return heightSlider.getValue();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataSelector dialog = new DataSelector(null);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                int herbivores = dialog.getHerbivores();
                int carnivores = dialog.getCarnivores();
                int width = dialog.getWidthValue();
                int height = dialog.getHeightValue();

                System.out.println("Liczba roślinożernych: " + herbivores);
                System.out.println("Liczba mięsożernych: " + carnivores);
                System.out.println("Szerokość planszy: " + width);
                System.out.println("Wysokość planszy: " + height);

                System.exit(0);
            }
        });
    }
}
