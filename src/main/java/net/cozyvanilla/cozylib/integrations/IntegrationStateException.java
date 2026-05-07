package net.cozyvanilla.cozylib.integrations;

import net.cozyvanilla.cozylib.runtime.Logger;

public class IntegrationStateException extends IllegalStateException {
    public IntegrationStateException(String message) {
        Logger.severe(message);
        //super(message);
    }
}