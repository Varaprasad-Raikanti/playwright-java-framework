package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();
    private static boolean isLoaded = false;

    /**
     * Loads default config from classpath (for backward compatibility)
     */
    private static void loadDefaultProperties() {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config/config.properties")) {
            if (input == null) {
                throw new RuntimeException("Default config file config/config.properties not found in classpath.");
            }
            properties.clear();
            properties.load(input);
            isLoaded = true;
            System.out.println("Default config loaded from classpath.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load default config.properties", e);
        }
    }

    /**
     * Loads config for a specific app like OXO, Hydroflask, etc.
     */
    public static void loadProperties(String appName) {
        String filePath = "src/test/resources/config/" + appName + "/config.properties";
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.clear();
            properties.load(fis);
            isLoaded = true;
            System.out.println("Config loaded for app: " + appName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config for app: " + appName, e);
        }
    }

    /**
     * Returns the value for the given property key.
     * Loads default config if not already loaded (for old scripts).
     */
    public static String getProperty(String key) {
        if (!isLoaded) {
            loadDefaultProperties(); // fallback to default
        }
        return properties.getProperty(key);
    }
}
