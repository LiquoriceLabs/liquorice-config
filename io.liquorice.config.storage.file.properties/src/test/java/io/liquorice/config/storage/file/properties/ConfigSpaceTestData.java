package io.liquorice.config.storage.file.properties;

import com.google.common.collect.ImmutableList;

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
    public static final String DOES_NOT_EXIST_KEY = "key.does.not.exist";

    public static final boolean BOOL_VALUE = true;
    public static final double DOUBLE_VALUE = 1.6;
    public static final int INT_VALUE = 7;
    public static final long LONG_VALUE = 17L;
    public static final String STRING_VALUE = "test string";

    public static final boolean DEFAULT_BOOL_VALUE = false;
    public static final double DEFAULT_DOUBLE_VALUE = 2.4;
    public static final int DEFAULT_INT_VALUE = 18;
    public static final long DEFAULT_LONG_VALUE = 24L;
    public static final String DEFAULT_STRING_VALUE = "default test value";
}
