package com.ui.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.Properties;

/**
 * Reads config.properties once and gives every class the same values.
 * PATTERN: Singleton (classic, eager) - one shared instance, created when the class is first used.
 */
public final class ConfigReader {

    // TODO 1: Stop anyone from extending this class (a subclass could create a second instance).
    //         Look up: final classes.

    // TODO 2: Add a constant for the file name ("config.properties").
    private static final String CONFIG_FILE = "config.properties";

    // TODO 3: Add the single instance: a private static final field, created right here (eager).
    //         Look up: eager vs lazy Singleton.
    private static final ConfigReader INSTANCE = new ConfigReader();

    // TODO 4: Add a private final field that holds the loaded key=value pairs.
    //         Never hand this object out to callers (encapsulation).
    private final Properties properties = new Properties();

    // TODO 5: Write a private constructor that loads the file from the classpath.
    //         - private so nobody outside can call "new ConfigReader()".
    //         - Use try-with-resources so the stream is always closed.
    //         - If the file isn't found, fail fast with a message that says which file and where.
    //         - Wrap the checked IOException in UncheckedIOException, keeping the original as the cause.
    //         Look up: ClassLoader.getResourceAsStream, Properties.load.
    private ConfigReader() {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (stream == null) {
                throw new IllegalStateException(CONFIG_FILE + " not found in src/test/resources");
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read " + CONFIG_FILE, e);
        }
    }

    // TODO 6: Write the public static method that returns the single instance.
    //         Think: why must it be static?
    public static ConfigReader getInstance() {
        return INSTANCE;
    }

    // TODO 7: Write a getter that returns a String value for a key.
    //         - Fail fast if the key is missing or blank, naming the key in the message.
    //         - Trim the value before returning it.
    //         Look up: String.isBlank vs isEmpty.
    // Layer 10: a -D system property wins over the file, e.g. "mvn test -Dheadless=true" on Jenkins,
    // so CI can change settings without editing config.properties.
    public String getString(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing key '" + key + "' in " + CONFIG_FILE);
        }
        return value.trim();
    }

    // Layer 10: strict true/false - a typo like "ture" fails fast instead of silently meaning false.
    public boolean getBoolean(String key) {
        String value = getString(key);
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(value);
        }
        throw new IllegalStateException(
                "Key '" + key + "' in " + CONFIG_FILE + " must be true or false, but was '" + value + "'");
    }

    // TODO 8: Write a getter that returns the timeout as a java.time.Duration.
    //         - Reuse the String getter from TODO 7 (DRY), then convert the number of seconds.
    //         - If the value isn't a number, catch NumberFormatException and throw a clearer exception
    //           naming the key and the bad value, with the original exception attached as the cause.
    //         Look up: Duration.ofSeconds, Integer.parseInt / Long.parseLong, exception chaining.
    public Duration getTimeout(String key) {
        String value = getString(key);
        try {
            return Duration.ofSeconds(Long.parseLong(value));
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "Key '" + key + "' in " + CONFIG_FILE + " must be a whole number of seconds, but was '" + value + "'", e);
        }
    }
}
