package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class CalculationService {

    private static final Logger logger = LoggerFactory.getLogger(CalculationService.class);

    /**
     * Медленный метод - последовательный стрим
     * Вычисляет сумму чисел от 1 до 1_000_000
     */
    public int calculateSumSequential() {
        logger.info("Was invoked method for calculate sum using SEQUENTIAL stream");
        long startTime = System.currentTimeMillis();

        int sum = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);

        long endTime = System.currentTimeMillis();
        logger.info("Sequential calculation took {} ms, result: {}", endTime - startTime, sum);
        return sum;
    }

    /**
     * БЫСТРЫЙ метод - параллельный стрим (ОПТИМИЗИРОВАННЫЙ)
     * Вычисляет сумму чисел от 1 до 1_000_000
     */
    public int calculateSumParallel() {
        logger.info("Was invoked method for calculate sum using PARALLEL stream");
        long startTime = System.currentTimeMillis();

        int sum = Stream.iterate(1, a -> a + 1)
                .parallel()
                .limit(1_000_000)
                .reduce(0, Integer::sum);

        long endTime = System.currentTimeMillis();
        logger.info("Parallel calculation took {} ms, result: {}", endTime - startTime, sum);
        return sum;
    }

    /**
     * СУПЕР БЫСТРЫЙ метод - математическая формула
     * O(1) сложность - самый оптимальный вариант
     */
    public int calculateSumFormula() {
        logger.info("Was invoked method for calculate sum using MATHEMATICAL FORMULA");
        long startTime = System.currentTimeMillis();

        // Формула суммы арифметической прогрессии: n * (n + 1) / 2
        int n = 1_000_000;
        int sum = n * (n + 1) / 2;

        long endTime = System.currentTimeMillis();
        logger.info("Formula calculation took {} ms, result: {}", endTime - startTime, sum);
        return sum;
    }
}