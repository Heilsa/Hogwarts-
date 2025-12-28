package ru.hogwarts.school;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Hogwarts School API",
                version = "1.0",
                description = "API для управления студентами и факультетами школы Хогвартс",
                contact = @Contact(
                        name = "Администрация Хогвартса",
                        email = "headmaster@hogwarts.ru"
                )
        )
)
public class SwaggerConfig {

}