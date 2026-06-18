package br.ufrn.pedrogalvao.atlas.mission;

import jakarta.validation.constraints.NotNull;

public record MissionStatusUpdateRequest(

        @NotNull(message = "Status é obrigatório")
        MissionStatus status

) {}
