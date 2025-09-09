package ru.berezhnov;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
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

        // Создаем график
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

    public static void printFunction(String functionName, double[] params) {
        Function<Double, Double> func1 = FunctionsBank.getFunctionByName(functionName, params);
        FunctionPrinter.plotFunction(func1, 0, 50, 0.01);
    }
}
