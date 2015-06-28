package io.liquorice.core.logging;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LogFactory {
    private static final String FACTORY_NAME = "liquorice-logging-factory";
    private static final String FACTORY_PROPERTIES = "liquorice-logging.properties";

    private static Logger logger;
    private static Properties properties;

    /**
     * private constructor
     */
    private LogFactory() {

    }

    /**
     * Initialize the Logger
     *
     * @param clazz
     *            The class to log messages on behalf of
     */
    public static synchronized Log getLog(Class clazz) {
        if (properties == null) {
            properties = new Properties();
            try {
                System.out.println(ClassLoader.getSystemResourceAsStream(FACTORY_PROPERTIES));
                properties.load(ClassLoader.getSystemResourceAsStream(FACTORY_PROPERTIES));
            } catch (Exception e) {
                System.err.println("Failed to load logging properties due to:");
                e.printStackTrace();
            }
        }

        Handler handler;
        logger = Logger.getLogger(FACTORY_NAME);
        try {
            handler = getHandler();
            handler.setFormatter(getFormatter());
            handler.setLevel(getLevel());

            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger due to:");
            e.printStackTrace();
        }

        return new Log(logger);
    }

    private static Handler getHandler() throws IOException {
        String handlerString = properties.getProperty(LoggingProperties.LOGGER_TYPE,
                LoggingProperties.DEFAULT_LOGGER_TYPE).toLowerCase();
        if (handlerString.equals("file")) {
            String filename = properties.getProperty(LoggingProperties.LOGGER_FILE,
                    LoggingProperties.DEFAULT_LOGGER_FILE);
            return new FileHandler(filename, true);
        } else {
            return new ConsoleHandler();
        }
    }

    private static Formatter getFormatter() {
        return new SingleLineFormatter();
    }

    private static Level getLevel() {
        String levelString = properties.getProperty(LoggingProperties.LOGGER_LEVEL,
                LoggingProperties.DEFAULT_LOGGER_LEVEL);
        return Level.parse(levelString);
    }
}
