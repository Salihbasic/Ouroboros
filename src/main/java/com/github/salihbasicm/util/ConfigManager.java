/*
MIT License

Copyright (c) 2020 Steinein_

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.github.salihbasicm.util;

import com.github.salihbasicm.sedexlives.LivesUser;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * Abstraction layer over the actual {@link FileConfiguration} used for the default config.
 */
public class ConfigManager {

    private FileConfiguration configuration;

    // Config paths

    private final String DEBUG_PATH = "debug";

    private final String USERNAME_PATH = "mysql.username";
    private final String PASSWORD_PATH = "mysql.password";
    private final String HOSTNAME_PATH = "mysql.hostname";
    private final String PORT_PATH = "mysql.port";
    private final String DATABASE_PATH = "mysql.database";

    private final String LIVES_MESSAGE_PATH = "livesMessage";
    private final String DEFAULT_LIVES_PATH = "defaultLives";

    // Constructor

    public ConfigManager(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    // Getters

    public boolean debugEnabled() {
        return configuration.getBoolean(DEBUG_PATH);
    }

    public String getUsername() {
        return configuration.getString(USERNAME_PATH);
    }

    public String getPassword() {
        return configuration.getString(PASSWORD_PATH);
    }

    public String getHostname() {
        return configuration.getString(HOSTNAME_PATH);
    }

    public String getPort() {
        return configuration.getString(PORT_PATH);
    }

    public String getDatabase() {
        return configuration.getString(DATABASE_PATH);
    }

    public int getDefaultLives() {
        return configuration.getInt(DEFAULT_LIVES_PATH);
    }

    /**
     * Gets the message with placeholders from the config.
     * If the PlaceholderAPI is installed, it will change all the placeholders to those from the API.
     * Since this plugin also provides placeholders for PlaceholderAPI, those are also changed.
     * If the PlaceholderAPI is not installed, it will only change the placeholders for %sedex_lives%.
     *
     * @param papiHooked Is the plugin hooked into PlaceholderAPI
     * @param user User of the lives system
     * @return Prepared lives message ready for further use
     */
    public String getLivesMessage(boolean papiHooked, LivesUser user) {
        final String livesMessageInConfig = Objects.requireNonNull(configuration.getString(LIVES_MESSAGE_PATH),
                                                            "livesMessage is null!");

        if (papiHooked)
            return PlaceholderAPI.setPlaceholders(user.getUser(), livesMessageInConfig);
        else
            return ChatColor.translateAlternateColorCodes('&',
                    livesMessageInConfig.replaceAll("%sedexlives_lives%", String.valueOf(user.getLives())))
                    .replaceAll("%sedexlives_maxlives%", String.valueOf(user.getMaxLives()));
    }

}
