package io.liquorice.config.utils;

import static java.util.Objects.requireNonNull;

/**
 * Utilities for validating and manipulating strings
 *
 * @author mthorpe
 */
public class StringUtils {

    /**
     * Verifies that {@code val} is neither null nor empty
     *
     * @param val
     *            the value to test
     * @return {@code val}
     * @throws IllegalArgumentException
     *             if {@code val} is null or empty
     */
    public static String requireNonEmpty(final String val) {
        return requireNonNull(emptyToNull(val));
    }

    /**
     * Verifies that {@code val} is neither null nor empty
     * 
     * @param val
     *            the value to test
     * @param message
     *            the message to pass as the exception if {@code val} is null or empty
     * @return {@code val}
     * @throws IllegalArgumentException
     *             if {@code val} is null or empty
     */
    public static String requireNonEmpty(final String val, final String message) {
        return requireNonNull(emptyToNull(val), message);
    }

    /**
     * Safely converts empty strings to nulls
     *
     * @param val
     *            the value to convert
     * @return {@code val} if non-null and non-empty, else null
     */
    public static String emptyToNull(final String val) {
        return "".equals(val) ? null : val;
    }
}
