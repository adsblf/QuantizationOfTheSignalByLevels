package ru.berezhnov;

import java.util.Map;
import java.util.function.Function;

public class FunctionsBank {
    private static Map<String, Function<double[], Function<Double, Double>>> functionBank = Map.of(
            "sin(n*x)", arr -> x -> Math.sin(arr[0] * x),
            "a*cos(n*x)", arr -> x -> arr[0] * Math.cos(arr[1] * x),
            "x^9 - 7x^7 + 15x^5 - 10x^3", arr -> x ->
                    Math.pow(x, 9) - 7 * Math.pow(x, 7) + 15 * Math.pow(x, 5) - 10 * Math.pow(x, 3)
    );

    public static Function<Double, Double> getFunctionByName(String name, double... params) {
        Function<double[], Function<Double, Double>> funcGenerator = functionBank.get(name);
        if (funcGenerator == null) {
            throw new IllegalArgumentException("Функция с именем '" + name + "' не найдена!");
        }

        return funcGenerator.apply(params);
    }
}
