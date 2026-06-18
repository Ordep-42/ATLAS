package br.ufrn.pedrogalvao.atlas.sensor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SensorCreateRequest(

        @NotBlank(message = "Nome do sensor é obrigatório")
        String name,

        @NotNull(message = "Tipo do sensor é obrigatório")
        SensorType type,

        @NotBlank(message = "Unidade é obrigatória")
        String unit

) {}