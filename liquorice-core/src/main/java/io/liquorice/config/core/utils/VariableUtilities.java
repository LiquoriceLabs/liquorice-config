package io.liquorice.config.core.utils;

/**
 * Convenience methods for manipulating property values
 */
public final class VariableUtilities {
    /**
     * Retrieve a value saved in an environment variable
     *
     * @param envVar
     *            The environment variable to retrieve the value of
     * @param defaultVal
     *            The default value to return if $envVar is undefined
     * @return $envVar if defined, else $defaultVal
     */
    public static String getFromEnv(String envVar, String defaultVal) {
        return getOrSetDefault(System.getenv(envVar), defaultVal);
    }

    /**
     * Safely get a variable's value or set a default if undefined
     *
     * @param testVal
     *            The ideal value
     * @param defaultVal
     *            The value to return if $testVal is undefined or empty
     * @return $testVal if defined and non-empty, else $defaultVal
     */
    public static String getOrSetDefault(String testVal, String defaultVal) {
        return testVal == null || testVal.equals("") ? defaultVal : testVal;
    }
}
