package br.ufrn.pedrogalvao.atlas.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "ATLAS API",
        version = "1.0",
        description = "API REST do sistema ATLAS (Acompanhamento de Telemetria e Logística para Sistemas Atmosféricos), destinada ao gerenciamento de missões, sensores e dados de telemetria em aplicações embarcadas atmosféricas e experimentais."
    )
)
public class OpenApiConfig {
	
}
