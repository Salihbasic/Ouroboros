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

package com.github.salihbasicm.ouroboros;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.salihbasicm.ouroboros.commands.OuroborosCommand;
import com.github.salihbasicm.ouroboros.hooks.PlaceholderapiExpansion;
import com.github.salihbasicm.ouroboros.lang.OuroborosMessage;
import com.github.salihbasicm.ouroboros.listeners.PlayerDeath;
import com.github.salihbasicm.ouroboros.listeners.PlayerJoin;
import com.github.salihbasicm.ouroboros.listeners.PlayerQuit;
import com.github.salihbasicm.ouroboros.storage.FlatfileStorageProvider;
import com.github.salihbasicm.ouroboros.storage.OuroborosStorage;
import com.github.salihbasicm.ouroboros.storage.MySQLStorageProvider;
import com.github.salihbasicm.ouroboros.storage.StorageType;
import com.github.salihbasicm.ouroboros.util.OuroborosConfig;
import com.github.salihbasicm.ouroboros.util.OuroborosUserCache;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class Ouroboros extends JavaPlugin {

    private OuroborosConfig ouroborosConfig;
    private OuroborosCache ouroborosCache;

    private OuroborosItemStorage itemStorage;
    private OuroborosUserStorage userStorage;

    private boolean papiHooked = false;

    private List<OuroborosUser> toggledOff;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        ouroborosConfig = new OuroborosConfig(this.getConfig());

        userStorage = new UserStorageFactory(this).getStorage(ouroborosConfig.getStorageType());
        itemStorage = new ItemStorageFactory(this).getStorage(ouroborosConfig.getStorageType());

        ouroborosCache = new OuroborosCache();

        hookIntoPlaceholderAPI();

        registerListeners();

        OuroborosCommand livesCommand = new OuroborosCommand(this);
        Objects.requireNonNull(this.getServer().getPluginCommand("lives")).setExecutor(livesCommand);

        toggledOff = new ArrayList<>();

    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerUseItem(this), this);
    }

    private void hookIntoPlaceholderAPI() {

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {

            papiHooked = true;
            new PlaceholderapiExpansion(this).register();
            this.getLogger().info("Successfully hooked into PlaceholderAPI.");

        } else {

            this.getLogger().warning("Could not hook into PlaceholderAPI. Some placeholders may not work!");

        }

    }

    /*
    Reloads the config and resets the livesConfig so it effectively gets reloaded too.
     */
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        ouroborosConfig = new OuroborosConfig(this.getConfig());
    }

    /**
     * Sends debug messages to console if debug is true in the config.
     *
     * @param message Debug message
     */
    public void debugMessage(String message) {

        if (getOuroborosConfig().debugEnabled())
            this.getLogger().log(Level.INFO, "[DEBUG]: " + message);

    }

    /**
     * Plugin's {@link OuroborosConfig}.
     *
     * @return OuroborosConfig for this plugin's instance
     */
    public OuroborosConfig getOuroborosConfig() {
        return ouroborosConfig;
    }

    /**
     * Represents all players that have toggled off the lives.
     *
     * @return HashMap of all players with toggled of lives
     */
    public List<OuroborosUser> getToggledOff() {
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

    public OuroborosCache getOuroborosCache() {
        return ouroborosCache;
    }

    public OuroborosItemStorage getOuroborosItemStorage() {
        return itemStorage;
    }

    public OuroborosUserStorage getStorage() {
        return userStorage;
    }

}
