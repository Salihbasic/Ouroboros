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

package sedexlives;

import commands.CommandManager;
import hooks.PlaceholderapiExpansion;
import listeners.PlayerQuit;
import org.bukkit.entity.Player;
import util.ConfigManager;
import util.SQLManager;
import listeners.PlayerDeath;
import listeners.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;

public class SedexLives extends JavaPlugin {

    private static SedexLives plugin = null;
    private ConfigManager configManager = null;
    private SQLManager sqlManager = null;

    private boolean papiHooked = false;

    private HashMap<Player, Boolean> toggledOff;

    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();

        configManager = new ConfigManager(this.getConfig());
        sqlManager = SQLManager.getSQLManager(this);

        sqlManager.setUpTable(); // If the table does not exist, it gets created. Nothing happens otherwise.

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiHooked = true;
            new PlaceholderapiExpansion().register();
            this.getLogger().info("Successfully hooked into PlaceholderAPI.");
        } else {
            this.getLogger().warning("Could not hook into PlaceholderAPI. Some placeholder may not work!");
        }

        registerListeners();

        CommandManager commandManager = new CommandManager();
        Objects.requireNonNull(this.getServer().getPluginCommand("lives")).setExecutor(commandManager);

        toggledOff = new HashMap<>();

    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
    }

    /*
    Reloads the config and resets the configManager so it effectively gets reloaded too.
     */
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        configManager = new ConfigManager(this.getConfig());
    }

    /**
     * Sends debug messages to console if debug is true in the config.
     *
     * @param message Debug message
     */
    public void debugMessage(String message) {

        if (getConfigManager().debugEnabled())
            plugin.getLogger().log(Level.INFO, "[DEBUG]: " + message);

    }

    /**
     * This plugin's instance.
     *
     * @return Plugin instance
     */
    public static SedexLives getSedexLives() {
        return plugin;
    }

    /**
     * Plugin's {@link ConfigManager}.
     *
     * @return ConfigManager for this plugin's instance
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Plugin's {@link SQLManager}
     *
     * @return SQLManager for this plugin's instance
     */
    public SQLManager getSqlManager() {
        return sqlManager;
    }

    /**
     * Represents all players that have toggled off the lives.
     *
     * @return HashMap of all players with toggled of lives
     */
    public HashMap<Player, Boolean> getToggledOff() {
        return toggledOff;
    }

    /**
     * Checks to see if the plugin is hooked into PlaceholderAPI.
     *
     * @return {@code true} if hooked, {@code false} otherwise
     */
    public boolean isPapiHooked() {
        return papiHooked;
    }

}
