package io.liquorice.config.test.support;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

/**
 * Created by mthorpe on 12/17/16.
 */
public class ConfigFormatterTestData {

    public static final String TEST_STRING = "this is a test string";
    public static final List<String> TEST_LIST = ImmutableList.of("string1", "string2");
    public static final Map<String, String> TEST_MAP = ImmutableMap.of("test", "json");

    public static final String TEST_JSON = "{\"test\":\"json\"}";
    public static final String TEST_NOT_JSON = "{ hello bob }";
}
