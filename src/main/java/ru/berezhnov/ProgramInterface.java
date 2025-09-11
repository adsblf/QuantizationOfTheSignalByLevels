package ru.berezhnov;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ProgramInterface {
    private final String[] functions = {
            "sin(n*x)",
            "a*cos(n*x)",
            "Пока заглушка"
    };

    private final List<String> quantizationTypes = List.of(
            "По верху",
            "По низу",
            "По среднему"
    );

    private final Map<String, List<String>> functionsWithLabels = Map.of(
            "sin(n*x)", List.of("Введите n: "),
            "a*cos(n*x)", List.of("Введите a: ", "Введите n: "),
            "Пока заглушка", List.of("Введите x: ")
    );

    /**
     * Параметры для построения графика:
     * functionFields - словарь, содержащий: "Название функции" : "Список ее параметров"
     * quantizationGroup - тип построения графика ("По верху", "По низу", "По центру")
     * quantLevelsField - уровень квантования
     */
    private final Map<String, List<JTextField>> functionFields = new HashMap<>();
    private ButtonGroup quantizationGroup;
    private JTextField quantLevelsField;

    public void createWindow() {
        JFrame frame = createFrame("Построить функцию с квантованием сигнала");

        JComboBox<String> comboBox = createComboBoxForSelectFunction(functions);
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

        frame.add(createPanelToSelectTheTypeOfQuantization(this.quantizationTypes), BorderLayout.CENTER);

        // Нижняя панель с кнопкой и полем для ввода уровней квантования
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        JPanel quantPanel = createPanelForEnteringQuantizationLevels();
        JButton plotTheFunctionGraph = createButtonForBuildingFunction("Построить график функции");
        bottomPanel.add(quantPanel);
        bottomPanel.add(plotTheFunctionGraph);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Действие при нажатии на кнопку "Построить график функции"
        plotTheFunctionGraph.addActionListener(_ -> {
            String selected = (String) comboBox.getSelectedItem();

            switch (selected) {
                case "sin(n*x)":
                    //FunctionPrinter.printFunction(selected, getFunctionParams(selected));
                    FunctionPrinter.printQuantizedFunction(selected, getFunctionParams(selected),
                            getQuantizationLevel(), getQuantizationType());
                    break;
                case "a*cos(n*x)":
                    dummyFunction2();
                    break;
                case "Пока заглушка":
                    dummyFunction3();
                    break;
            }
        });

        frame.setVisible(true);
    }

    /**
     * Создает новое окно для программы с переданным названием
     *
     * @param frameName
     * @return JFrame
     */
    private JFrame createFrame(String frameName) {
        return new JFrame(frameName) {{
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(500, 400);
            setLayout(new BorderLayout());
        }};
    }

    /**
     * Создает выпадающий список для выбора нужной функции
     *
     * @param functions
     * @return JComboBox<>
     */
    private JComboBox<String> createComboBoxForSelectFunction(String[] functions) {
        return new JComboBox<>(functions);
    }

    /**
     * Создает панель для отдельной функции с необходимыми полями для ввода
     *
     * @param functionName
     * @param labels
     * @return JPanel panel
     */
    private JPanel createFunctionPanel(String functionName, List<String> labels) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        List<JTextField> fields = new ArrayList<>();

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

    /**
     * Создает панель для ввода уровней квантования
     *
     * @return JPanel
     */
    private JPanel createPanelForEnteringQuantizationLevels() {
        return new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
            add(new JLabel("Установить количество уровней квантования:"));
            quantLevelsField = new JTextField("5", 10); // значение по умолчанию = 5
            add(quantLevelsField);
        }};
    }

    /**
     * Создает кнопку для построения графика функции
     *
     * @param functionName
     * @return JButton
     */
    private JButton createButtonForBuildingFunction(String functionName) {
        return new JButton(functionName) {{
            setBackground(Color.PINK);
            setFont(new Font("Arial", Font.BOLD, 12));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        }};
    }

    /**
     * Создает список для выбора типа построения графика из переданного списка строк
     *
     * @param quantizationTypes
     * @return JPanel radioPanel
     */
    private JPanel createPanelToSelectTheTypeOfQuantization(List<String> quantizationTypes) {
        if (quantizationTypes.isEmpty()) {
            return new JPanel();
        }

        List<JRadioButton> radioButtons = new ArrayList<>();
        this.quantizationGroup = new ButtonGroup();

        quantizationTypes.forEach(quantizationType -> {
            JRadioButton jRadioButton = new JRadioButton(quantizationType);
            jRadioButton.setActionCommand(quantizationType);
            radioButtons.add(jRadioButton);
            this.quantizationGroup.add(jRadioButton);
        });

        radioButtons.getFirst().setSelected(true);

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        radioButtons.forEach(radioPanel::add);

        return radioPanel;
    }

    private void dummyFunction2() {
        printFunctionData("a*cos(n*x)");
    }

    private void dummyFunction3() {
        printFunctionData("Пока заглушка");
    }

    private double[] getFunctionParams(String functionName) {
        List<JTextField> fields = functionFields.get(functionName);
        List<Double> values = new ArrayList<>();
        fields.forEach(field -> {
            values.add(Double.parseDouble(field.getText()));
        });
        return values.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private int getQuantizationLevel() {
        return Integer.parseInt(quantLevelsField.getText());
    }

    private String getQuantizationType() {
        return quantizationGroup.getSelection().getActionCommand();
    }

    private void printFunctionData(String functionName) {
        List<JTextField> fields = functionFields.get(functionName);
        System.out.println("Параметры " + functionName + ":");
        for (int i = 0; i < fields.size(); i++) {
            System.out.println("  Поле " + (i + 1) + ": " + fields.get(i).getText());
        }

        String selectedQuant = quantizationGroup.getSelection().getActionCommand();
        System.out.println("Выбранный тип квантования: " + selectedQuant);

        String levels = quantLevelsField.getText();
        System.out.println("Количество уровней квантования: " + levels);
    }
}
