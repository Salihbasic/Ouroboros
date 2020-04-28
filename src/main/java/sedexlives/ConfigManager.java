package sedexlives;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Abstraction layer over the actual {@link FileConfiguration} used for the default config.
 */
public class ConfigManager {

    private FileConfiguration configuration;

    // Config paths

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
     * @param player Player whom the placeholders refer to
     * @param lives Lives the player has
     * @return Prepared lives message ready for further use
     */
    public String getLivesMessage(boolean papiHooked, Player player, int lives) {
        final String livesMessageInConfig = configuration.getString(LIVES_MESSAGE_PATH);

        if (papiHooked)
            return PlaceholderAPI.setPlaceholders(player, livesMessageInConfig);
        else
            return ChatColor.translateAlternateColorCodes('&',
                    livesMessageInConfig.replaceAll("%sedexlives_lives%", String.valueOf(lives)));
    }

}
