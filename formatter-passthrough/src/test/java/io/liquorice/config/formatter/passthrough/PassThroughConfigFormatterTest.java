package io.liquorice.config.formatter.passthrough;

import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_LIST;
import static io.liquorice.config.test.support.ConfigFormatterTestData.TEST_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Created by mthorpe on 12/11/16.
 */
class PassThroughConfigFormatterTest {

    private PassThroughConfigFormatter configFormatter;

    @BeforeEach
    void setup() {
        configFormatter = new PassThroughConfigFormatter();
    }

    @Test
    void testReadWriteSimple() {
        final Object written = configFormatter.write(TEST_VALUE);
        assertEquals(TEST_VALUE, configFormatter.read(written, String.class).get());
    }

    @Test
    void testReadWriteComplex() {
        final Object written = configFormatter.write(TEST_LIST);
        assertEquals(TEST_LIST, configFormatter.read(written, List.class).get());
    }

    @Test
    void testReadFailure() {
        final Object written = configFormatter.write(TEST_VALUE);
        assertThrows(ClassCastException.class, () -> configFormatter.read(written, List.class));
    }
}
