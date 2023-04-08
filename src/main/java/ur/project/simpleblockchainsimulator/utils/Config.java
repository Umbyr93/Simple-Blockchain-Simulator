package ur.project.simpleblockchainsimulator.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Config {
    private static PropertiesConfiguration instance;

    private static void newInstance() {
        instance = new PropertiesConfiguration();
        try {
            instance.load("application.properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException("Configuration not found");
        }
    }

    public static Integer getInt(String propName) {
        if(instance == null)
            Config.newInstance();

        return instance.getInt(propName);
    }

    public static String getString(String propName) {
        if(instance == null)
            Config.newInstance();

        return instance.getString(propName);
    }
}
