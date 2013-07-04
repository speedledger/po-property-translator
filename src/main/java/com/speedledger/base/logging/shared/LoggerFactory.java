package com.speedledger.base.logging.shared;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This class initializes java-util-logging with the configuration read from logging.properties.
 */
public class LoggerFactory {
    static {
        setupLog();
    }
    public static void setupLog() {
        String logSetup = "/logging.properties";
        InputStream fis = logSetup.getClass().getResourceAsStream("/logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getClass().getName());
    }
}
