package com.github.salihbasicm.sedexlives.lang;

import com.github.salihbasicm.sedexlives.LivesUser;
import com.github.salihbasicm.sedexlives.SedexLives;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

public class MessageManager {

    private static final String FILE_NAME = "messages.yml";

    private final SedexLives plugin;

    private File msgFile;
    private FileConfiguration msgConf;

    public MessageManager(final SedexLives plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("Loaded messages.yml");
    }

    /**
     * Returns a fully formatted message, supporting all placeholders.
     *
     * @param user Message target (used for placeholders)
     * @param message Message
     * @return Fully formatted message
     */
    public String getMessage(final LivesUser user, final Message message) {
        return formatMessage(user, message);
    }

    /**
     * Returns a message only formatted for chat colours as provided by {@link ChatColor}.
     *
     * @param message Message
     * @return Messages formatted for chat colours
     */
    public String getSimpleMessage(final Message message) {
        return getSimpleFormatMessage(message);
    }

    /**
     * <p>
     *    Attempts to parse a message and return its results.
     *    If PlaceholderAPI is enabled, it will pass the message, together with the user, to PlaceholderAPi and let
     *    it format the message instead, returning the result.
     * </p>
     *
     * <p>
     *    If PlaceholderAPI is disabled, it will format the message with all the natively supported placeholders,
     *    as well as chat colour coding.
     * </p>
     *
     * @param user Message target (used for placeholders)
     * @param message Message to be formatted
     * @return Fully formatted message
     */
    private String formatMessage(final LivesUser user, final Message message) {

        if (plugin.isPapiHooked())
            return PlaceholderAPI.setPlaceholders(user.getUser(), getMessageFromPath(message.getPath()));
        else
            return ChatColor.translateAlternateColorCodes('&',
                    getMessageFromPath(message.getPath())
                            .replaceAll("%sedexlives_lives%", String.valueOf(user.getLives())))
                            .replaceAll("%sedexlives_maxlives%", String.valueOf(user.getMaxLives()));
    }

    /**
     * Takes the message and formats only chat colour codes. Allows plugin users to use '&' as is standard for
     * most Spigot plugins.
     * @param message Message to be formatted
     * @return Messages formatted for chat colours
     */
    private String getSimpleFormatMessage(final Message message) {

        return ChatColor.translateAlternateColorCodes('&', getMessageFromPath(message.getPath()));

    }

    /**
     * Returns the message from {@code message.yml} based on the value specified in {@link Message}.
     *
     * @param path Message path
     * @return Message from {@code message.yml}
     */
    private String getMessageFromPath(final String path) {
        return getMessagesConfig().getString(path);
    }

    /**
     * Saves the default values of {@code messages.yml} if the file doesn't exist.
     */
    public void saveDefaultMessages() {

        if (msgFile == null)
            msgFile = new File(plugin.getDataFolder(), FILE_NAME);

        if (!msgFile.exists())
            plugin.saveResource(FILE_NAME, false);

        reloadMessages();
    }

    /**
     * Reloads the messages.
     */
    public void reloadMessages() {
        msgConf = YamlConfiguration.loadConfiguration(msgFile);
    }

    /**
     * Attempts to return a reference to {@code messages.yml}.
     * @return Reference to {@code messages.yml}
     */
    public FileConfiguration getMessagesConfig() {
        return Objects.requireNonNull(msgConf, "Could not load messages.yml!");
    }


}
