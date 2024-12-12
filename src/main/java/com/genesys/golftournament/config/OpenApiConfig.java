package com.genesys.golftournament.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(
        contact = @Contact(
                name = "Ademola Kazeem",
                email = "w.ademola.kazeem@gmail.com",
                url = "https://github.com/ademolakazeem"
        ),
        title = "Golf Tournament API",
        version = "1.0",
        description = "API for managing golf tournament scores and leaderboard",
        termsOfService = "Terms of service"
),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local Server"
                )
        })
public class OpenApiConfig {

}
