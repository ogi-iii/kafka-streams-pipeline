package kafka.streams.pipeline.properties;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * utils for getting and loading properties
 */
public class PropertyUtils {
    private static final String INIT_FILE_PATH = "app.properties";
    private static final Logger logger = Logger.getLogger(PropertyUtils.class.getName());

    private static Properties properties;

    /**
     * initialize the properties by loading app property file
     */
    private static void initialize() {
        properties = new Properties();
        try (var inputStream = ClassLoader.getSystemResourceAsStream(INIT_FILE_PATH)) {
            properties.load(inputStream);
            logger.info("Properties were initialized.");
        } catch (IOException e) {
            logger.severe(String.format("failed to load properties from: %s", INIT_FILE_PATH));
        }
    }

    /**
     * get the property related to the key
     *
     * @param key property key
     * @return property value
     */
    public static String getProperty(final String key) {
        return getProperty(key, "");
    }

    /**
     * get the property related to the key
     *
     * @param key property key
     * @param defaultValue default property value
     * @return property value or default value
     */
    public static String getProperty(final String key, final String defaultValue) {
        if (properties == null) {
            initialize();
        }
        return properties.getProperty(key, defaultValue);
    }
}
