package io.liquorice.core.logging;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * SingleLineFormatter formats the LogRecord as follows:
 * date   level   localized message with parameters
 */
public class SingleLineFormatter extends Formatter {
    /**
     * Default constructor for a SingleLineFormatter
     */
    public SingleLineFormatter() {
        super();
    }

    /**
     * Format a LogRecord to the specified format above
     *
     * @param	record
     * @return	The constructed log message
     */
    public String format(LogRecord record) {
        Date date = new Date(record.getMillis());
        return date.toString() + " [" + record.getLevel().getName().toUpperCase() + "] " + formatMessage(record);
    }
}
