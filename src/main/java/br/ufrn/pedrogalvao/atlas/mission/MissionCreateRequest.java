package br.ufrn.pedrogalvao.atlas.mission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MissionCreateRequest(

        @NotBlank(message = "Nome da missão é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String name,

        @Size(max = 100, message = "Descrição deve ter no máximo 100 caracteres")
        String description

) {}
