package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.CalculationService;

@RestController
@RequestMapping("/info")
@Tag(name = "Информация", description = "API для получения информации о приложении")
public class InfoController {

    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Value("${server.port}")
    private int serverPort;

    private final CalculationService calculationService;

    public InfoController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @GetMapping("/port")
    @Operation(summary = "Получить порт, на котором запущено приложение")
    public String getPort() {
        logger.info("Was invoked method for get server port");
        return "Application is running on port: " + serverPort;
    }

    @GetMapping("/sum/parallel")
    @Operation(summary = "Вычислить сумму чисел от 1 до 1_000_000 (параллельный стрим - ОПТИМИЗИРОВАНО)")
    public ResponseEntity<String> calculateSumParallel() {
        logger.info("Request to calculate sum using parallel stream (OPTIMIZED)");
        long startTime = System.currentTimeMillis();

        int sum = calculationService.calculateSumParallel();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return ResponseEntity.ok(String.format(
                "Сумма (параллельный стрим): %d%nВремя выполнения: %d мс%nСтатус: ⚡ ОПТИМИЗИРОВАНО!",
                sum, duration
        ));
    }

    @GetMapping("/sum/formula")
    @Operation(summary = "Вычислить сумму чисел от 1 до 1_000_000 (математическая формула - САМЫЙ БЫСТРЫЙ)")
    public ResponseEntity<String> calculateSumFormula() {
        logger.info("Request to calculate sum using mathematical formula");
        long startTime = System.currentTimeMillis();

        int sum = calculationService.calculateSumFormula();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return ResponseEntity.ok(String.format(
                "Сумма (формула): %d%nВремя выполнения: %d мс%nСтатус: 🚀 САМЫЙ БЫСТРЫЙ!",
                sum, duration
        ));
    }
}