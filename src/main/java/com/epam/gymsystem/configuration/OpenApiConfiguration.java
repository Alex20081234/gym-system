package com.epam.gymsystem.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Gym System API");
        Contact myContact = new Contact();
        myContact.setName("Oleksandr Raskosov");
        myContact.setEmail("raskosov.alex.jr@gmail.com");
        Info information = new Info()
                .title("Gym System API")
                .version("1.0")
                .description("This API exposes endpoints of gym system application.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
