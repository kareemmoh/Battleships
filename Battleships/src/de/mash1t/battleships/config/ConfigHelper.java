/*
 * The MIT License
 *
 * Copyright 2015 Manuel Schmid.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.mash1t.battleships.config;

/**
 * Helper for config params
 *
 * @author Manuel Schmid
 */
public class ConfigHelper {

    // Config controller
    private static final ConfigController conf = new ConfigController("Config.ini");

    // Config Params
    private static boolean devMode;
    private static boolean devModeHover;
    private static int port;

    public static void init() {

        if (!conf.readConfigFile() || !conf.validateConfig()) {
            System.out.println("Server configuration file not found/readable/valid");
            conf.makeDefaultFile();
            System.out.println("Restored default configuration");
            conf.readConfigFile();
        }

        devMode = conf.getConfigValueBoolean(ConfigParam.DevMode);
        devModeHover = conf.getConfigValueBoolean(ConfigParam.DevModeHover);
        port = conf.getConfigValueInt(ConfigParam.Port);
    }

    /**
     * Getter for devmode
     *
     * @return is dev mode enabled
     */
    public static boolean isDevMode() {
        return devMode;
    }

    /**
     * Getter for hover devmode
     *
     * @return is hover dev mode enabled
     */
    public static boolean isDevModeHover() {
        return devModeHover;
    }

    /**
     * Getter for port
     *
     * @return port set in config
     */
    public static int getPort() {
        return port;
    }

    /**
     * Outputs a line on the console if dev mode is enabled
     *
     * @param line String to output on the console
     */
    public static void devLine(String line) {
        if (devMode) {
            System.out.println(line);
        }
    }
}
