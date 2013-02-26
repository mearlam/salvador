package com.salvador.configuration;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mearlam
 * Date: 26/02/13
 * Time: 07:46
 */
public class ConfigurationManager {

    static final Logger log = LoggerFactory.getLogger(ConfigurationManager.class);
    private static final String CONFIG_FOLDER = "config";
    private static final String SETTINGS_FILE = "settings.xml";

    XStream xStream;

    public ConfigurationManager() {
        xStream = new XStream();
    }

    public void save(Configuration configuration) throws IOException {
        final String xml = xStream.toXML(configuration);
        final String settingsFolder =
                        configuration.getHome() + File.separator + CONFIG_FOLDER + File.separator;
        final String settingsPath = settingsFolder + SETTINGS_FILE;
        FileUtils.forceMkdir(new File(settingsFolder));
        log.debug("saving config to {}", settingsPath);
        FileUtils.writeStringToFile(new File(settingsPath), xml);
    }

    public Configuration load(String home) {

        Configuration configuration = new Configuration();
        final String settingsPath = home + File.separator + CONFIG_FOLDER + File.separator + SETTINGS_FILE;
        log.debug("loading configuration from {}", settingsPath);
        final File settings = new File(settingsPath);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(settings.getAbsolutePath()
                    + File.separator + "content.xml");
            configuration = (Configuration) xStream.fromXML(inputStream);
        } catch (FileNotFoundException e) {
            log.error("could not load settings", e);
        }finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignore) {

                }
            }
        }

        return configuration;
    }
}
