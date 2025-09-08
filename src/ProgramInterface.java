import javax.swing.*;
import java.awt.*;

public class ProgramInterface {
    private final String[] functions = {
            "Функция 1",
            "Функция 2",
            "Функция 3"
    };

    private final JTextField textFieldX = new JTextField(20);
    private final JTextField textFieldY = new JTextField(20);
    private final JTextField textFieldN = new JTextField(20);

    public void createWindow() {
        JFrame frame = new JFrame("Построить функцию с квантованием сигнала");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        JComboBox<String> comboBox = new JComboBox<>(functions);
        frame.add(comboBox, BorderLayout.NORTH);

        CardLayout layout = new CardLayout();
        JPanel panel = new JPanel(layout);

        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Введите x:"));
        panel1.add(textFieldX);

        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("Введите x:"));
        panel2.add(textFieldY);

        JPanel panel3 = new JPanel();
        panel3.add(new JLabel("Введите x:"));
        panel3.add(textFieldN);

        panel.add(panel1, "Функция 1");
        panel.add(panel2, "Функция 2");
        panel.add(panel3, "Функция 3");

        frame.add(panel, BorderLayout.WEST);

        comboBox.addActionListener(action -> {
            String selected = (String) comboBox.getSelectedItem();
            layout.show(panel, selected);
        });

        JButton plotTheFunctionGraph = new JButton("Построить график функции");
        plotTheFunctionGraph.setBackground(Color.PINK);
        plotTheFunctionGraph.setFont(new Font("Arial", Font.BOLD, 12));
        plotTheFunctionGraph.setPreferredSize(new Dimension(400, 50));
        frame.add(plotTheFunctionGraph, BorderLayout.SOUTH);

        plotTheFunctionGraph.addActionListener(action -> {
            String selected = (String) comboBox.getSelectedItem();

            switch(selected) {
                case "Функция 1":
                    dummyFunction1();
                    break;
                case "Функция 2":
                    dummyFunction2();
                    break;
                case "Функция 3":
                    dummyFunction3();
                    break;
            }
        });

        frame.setVisible(true);
    }

    private void dummyFunction1() {
        System.out.println("Вызвана функция 1");
    }

    private void dummyFunction2() {
        System.out.println("Вызвана функция 2");
    }

    private void dummyFunction3() {
        System.out.println("Вызвана функция 3");
    }
}
