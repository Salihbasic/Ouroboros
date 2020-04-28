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
