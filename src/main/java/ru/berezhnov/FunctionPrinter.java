package ru.berezhnov;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

public class FunctionPrinter {

    /**
     * Построение графика функции func на отрезке [xMin, xMax] с шагом step
     * @param func Функция от x
     * @param xMin Минимальное значение X
     * @param xMax Максимальное значение X
     * @param step Шаг по X
     */
    private static void plotFunction(Function<Double, Double> func, double xMin, double xMax, double step) {
        // Создаем серию данных
        XYSeries series = new XYSeries("f(x)");

        // Заполняем серию точками
        for (double x = xMin; x <= xMax; x += step) {
            double y = func.apply(x);
            series.add(x, y);
        }

        // Коллекция для графика
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        // Создаем оси
        JFreeChart chart = ChartFactory.createXYLineChart(
                "График функции",
                "x",
                "y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Получаем XYPlot
        XYPlot plot = chart.getXYPlot();

        // Настраиваем ось X
        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setAutoRangeIncludesZero(true);
        xAxis.setLowerMargin(0);
        xAxis.setUpperMargin(0);
        xAxis.setTickMarksVisible(true);

        // Настраиваем ось Y
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setAutoRangeIncludesZero(true);
        yAxis.setLowerMargin(0);
        yAxis.setUpperMargin(0);
        yAxis.setTickMarksVisible(true);

        // Перемещаем оси к пересечению в (0,0)
        plot.setDomainZeroBaselineVisible(true);  // вертикальная ось через x=0
        plot.setRangeZeroBaselineVisible(true);   // горизонтальная ось через y=0

        // Включаем сетку
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);

        // Настройка линии графика: только линия, без точек
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);

        // Панель для отображения
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        chartPanel.setMouseWheelEnabled(true); // масштабирование колесиком мыши

        // Создаем окно
        JFrame frame = new JFrame("График функции");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Построение сравнения функции и её квантованной версии
     * @param func Функция
     * @param xMin Минимум X
     * @param xMax Максимум X
     * @param step Шаг по X
     * @param nLevels Уровни квантования
     * @param quantType "верх", "низ", "середина"
     */
    public static void plotQuantizedFunction(Function<Double, Double> func,
                                               double xMin, double xMax, double step,
                                               int nLevels, String quantType) {
        XYSeries originalSeries = new XYSeries("Оригинал");
        XYSeries quantizedSeries = new XYSeries("Квантованная");

        // Найти экстремумы + построить обычный график
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (double x = xMin; x <= xMax; x += step) {
            double y = func.apply(x);
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
            originalSeries.add(x, y);
        }

        double qStep = (maxY - minY) / (nLevels);

        // Построение квантованного графика
        for (double x = xMin; x <= xMax; x += step) {
            double y = func.apply(x);
            double qY = switch (quantType.toLowerCase()) {
                case "по верху" -> Math.ceil((y - minY) / qStep) * qStep + minY;
                case "по низу" -> Math.floor((y - minY) / qStep) * qStep + minY;
                default -> Math.round((y - minY) / qStep) * qStep + minY;
            };
            quantizedSeries.add(x, qY);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(originalSeries);
        dataset.addSeries(quantizedSeries);

        // Настройка осей
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Сравнение функции и квантованной версии",
                "x",
                "y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        // Настройка цветов линий
        XYDifferenceRenderer renderer = new XYDifferenceRenderer(Color.RED, Color.BLUE, false);
        renderer.setSeriesPaint(0, Color.RED);   // оригинал
        renderer.setSeriesPaint(1, Color.BLUE);  // квантованная
        plot.setRenderer(renderer);

        // Настройка осей и сетки
        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        xAxis.setAutoRangeIncludesZero(true);
        yAxis.setAutoRangeIncludesZero(true);
        plot.setDomainZeroBaselineVisible(true);
        plot.setRangeZeroBaselineVisible(true);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);

        // Панель и окно
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        chartPanel.setMouseWheelEnabled(true);

        JFrame frame = new JFrame("Квантованное сравнение");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public static void printFunction(String functionName, double[] params, double xMin, double xMax) {
        Function<Double, Double> func = FunctionsBank.getFunctionByName(functionName, params);
        FunctionPrinter.plotFunction(func, xMin, xMax, 0.01);
    }

    public static void printQuantizedFunction(String functionName, double[] params, int quantizationLevels,
                                              String quantizationType, double xMin, double xMax) {
        Function<Double, Double> func = FunctionsBank.getFunctionByName(functionName, params);
        FunctionPrinter.plotQuantizedFunction(func, xMin, xMax, 0.01, quantizationLevels, quantizationType);
    }
}
