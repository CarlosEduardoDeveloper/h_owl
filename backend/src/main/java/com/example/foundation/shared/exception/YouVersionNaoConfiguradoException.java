package com.example.foundation.shared.exception;

public class YouVersionNaoConfiguradoException extends RuntimeException {

    public YouVersionNaoConfiguradoException() {
        super("Integração YouVersion não configurada. Defina a variável YVP_APP_KEY.");
    }
}
