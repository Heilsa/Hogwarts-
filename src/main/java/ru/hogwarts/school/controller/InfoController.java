package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
@Tag(name = "Информация", description = "API для получения информации о приложении")
public class InfoController {

    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Value("${server.port}")
    private int serverPort;

    @GetMapping("/port")
    @Operation(summary = "Получить порт, на котором запущено приложение")
    public String getPort() {
        logger.info("Was invoked method for get server port");
        logger.debug("Returning port: {}", serverPort);
        return "Application is running on port: " + serverPort;
    }
}