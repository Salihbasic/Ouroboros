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
import database.SQLManager;
import listeners.PlayerDeath;
import listeners.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SedexLives extends JavaPlugin {

    private static SedexLives plugin = null;
    private ConfigManager configManager = null;
    private SQLManager sqlManager = null;
    private CommandManager commandManager = null;

    private boolean papiHooked = false;

    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();

        configManager = new ConfigManager(this.getConfig());
        sqlManager = SQLManager.getSQLManager(this);

        sqlManager.setUpTable(); // If the table does not exist, it gets created. Nothing happens otherwise.

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiHooked = true;
            new SedexLivesExpansion().register();
            this.getLogger().info("Successfully hooked into PlaceholderAPI.");
        } else {
            this.getLogger().warning("Could not hook into PlaceholderAPI. Some placeholder may not work!");
        }

        registerListeners();

        commandManager = new CommandManager();
        this.getServer().getPluginCommand("lives").setExecutor(commandManager);

    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
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

    public CommandManager getCommandManager() {
        return commandManager;
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
