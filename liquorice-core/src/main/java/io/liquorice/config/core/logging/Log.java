package io.liquorice.config.core.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * Created by mthorpe on 6/16/15.
 */
public class Log {
    private Logger logger;

    public Log(Logger logger) {
        this.logger = logger;
    }

    /**
     * Log a message with level "SEVERE"
     *
     * @param log
     *            The message to log
     */
    public synchronized void severe(String log) {
        logger.severe(log);
    }

    /**
     * Log a message with level "WARNING"
     *
     * @param log
     *            The message to log
     */
    public synchronized void warning(String log) {
        logger.warning(log);
    }

    /**
     * Log a message with level "INFO"
     *
     * @param log
     *            The message to log
     */
    public synchronized void info(String log) {
        logger.info(log);
    }

    /**
     * Log a message with level "CONFIG"
     *
     * @param log
     *            The message to log
     */
    public synchronized void config(String log) {
        logger.config(log);
    }

    /**
     * Log an exception stack trace with level "SEVERE"
     *
     * @param e
     *            The exception to log
     */
    public synchronized void severe(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        pw.close();
        logger.severe(sw.toString());
    }

    /**
     * Log an exception stack trace with level "SEVERE"
     *
     * @param e
     *            The exception to log
     */
    public synchronized void warning(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        pw.close();
        logger.warning(sw.toString());
    }
}
