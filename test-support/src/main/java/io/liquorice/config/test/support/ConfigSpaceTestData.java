package io.liquorice.config.test.support;

import java.util.List;

/**
 * Created by mthorpe on 12/13/16.
 */
public final class ConfigSpaceTestData {

    public static final String BOOL_KEY = "key.bool";
    public static final String DOUBLE_KEY = "key.double";
    public static final String INT_KEY = "key.int";
    public static final String LONG_KEY = "key.long";
    public static final String STRING_KEY = "key.string";
    public static final String COMPLEX_KEY = "key.complex";
    public static final String DOES_NOT_EXIST_KEY = "key.does.not.exist";

    public static final boolean BOOL_VALUE = true;
    public static final double DOUBLE_VALUE = 1.6;
    public static final int INT_VALUE = 7;
    public static final long LONG_VALUE = 17L;
    public static final String STRING_VALUE = "test string";
    public static final List<String> COMPLEX_VALUE = List.of(BOOL_KEY, DOUBLE_KEY);

    public static final boolean DEFAULT_BOOL_VALUE = false;
    public static final double DEFAULT_DOUBLE_VALUE = 2.4;
    public static final int DEFAULT_INT_VALUE = 18;
    public static final long DEFAULT_LONG_VALUE = 24L;
    public static final String DEFAULT_STRING_VALUE = "default test value";
    public static final List<String> DEFAULT_COMPLEX_VALUE = List.of(INT_KEY, LONG_KEY);

    public static final boolean UPDATED_BOOL_VALUE = false;
    public static final double UPDATED_DOUBLE_VALUE = 14.5;
    public static final int UPDATED_INT_VALUE = 9;
    public static final long UPDATED_LONG_VALUE = 31L;
    public static final String UPDATED_STRING_VALUE = "updated string";
    public static final List<String> UPDATED_COMPLEX_VALUE = List.of(INT_KEY, STRING_KEY);

    public static final String JSON_STRING = "{" + //
            String.format("\"%s\":%s,", BOOL_KEY, Boolean.toString(BOOL_VALUE)) + //
            String.format("\"%s\":%f,", DOUBLE_KEY, DOUBLE_VALUE) + //
            String.format("\"%s\":%d,", INT_KEY, INT_VALUE) + //
            String.format("\"%s\":%d,", LONG_KEY, LONG_VALUE) + //
            String.format("\"%s\":\"%s\",", STRING_KEY, STRING_VALUE) + //
            String.format("\"%s\":[\"%s\",\"%s\"]", COMPLEX_KEY, BOOL_KEY, DOUBLE_KEY) + //
            "}";
}
