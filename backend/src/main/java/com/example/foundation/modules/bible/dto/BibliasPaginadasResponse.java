package com.example.foundation.modules.bible.dto;

import java.util.List;

public record BibliasPaginadasResponse(
        List<BibliaResumoResponse> dados,
        String proximoPageToken
) {
}
