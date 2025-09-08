import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ProgramInterface {
    private final String[] functions = {
            "Функция 1",
            "Функция 2",
            "Функция 3"
    };

    private final Map<String, List<String>> functionsWithLabels = Map.of(
            "Функция 1", List.of("Введите x: ", "Введите y: "),
            "Функция 2", List.of("Введите z: ", "Введите s: "),
            "Функция 3", List.of("Введите x: ")
    );

    private final Map<String, List<JTextField>> functionFields = new HashMap<>();

    public void createWindow() {
        JFrame frame = new JFrame("Построить функцию с квантованием сигнала");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        JComboBox<String> comboBox = new JComboBox<>(functions);
        frame.add(comboBox, BorderLayout.NORTH);

        CardLayout layout = new CardLayout();
        JPanel panel = new JPanel(layout);

        functionsWithLabels.forEach((functionName, fields) -> {
            panel.add(createFunctionPanel(functionName, fields), functionName);
        });

        frame.add(panel, BorderLayout.WEST);

        comboBox.addActionListener(_ -> {
            String selected = (String) comboBox.getSelectedItem();
            layout.show(panel, selected);
        });

        JButton plotTheFunctionGraph = new JButton("Построить график функции");
        plotTheFunctionGraph.setBackground(Color.PINK);
        plotTheFunctionGraph.setFont(new Font("Arial", Font.BOLD, 12));
        plotTheFunctionGraph.setPreferredSize(new Dimension(400, 50));
        frame.add(plotTheFunctionGraph, BorderLayout.SOUTH);

        plotTheFunctionGraph.addActionListener(_ -> {
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

    private JPanel createFunctionPanel(String functionName, List<String> labels) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        List <JTextField> fields = new ArrayList<>();

        labels.forEach(label -> {
            JTextField field = new JTextField(20);
            field.setMaximumSize(field.getPreferredSize()); // Ёбанный костыль

            panel.add(new JLabel(label));
            panel.add(field);

            fields.add(field);
        });

        this.functionFields.put(functionName, fields);
        return panel;
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
