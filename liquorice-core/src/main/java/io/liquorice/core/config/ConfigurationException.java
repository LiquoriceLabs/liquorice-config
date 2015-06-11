package io.liquorice.core.config;

/**
 * Created by mthorpe on 6/10/15.
 */
public class ConfigurationException extends IllegalArgumentException {
    public ConfigurationException(final String message) {
        super(message);
    }

    public ConfigurationException(final String message, final Exception ex) {
        super(message, ex);
    }
}
