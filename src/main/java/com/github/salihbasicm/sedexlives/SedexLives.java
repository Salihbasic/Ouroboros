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

package com.github.salihbasicm.sedexlives;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.salihbasicm.sedexlives.commands.LivesCommand;
import com.github.salihbasicm.sedexlives.hooks.PlaceholderapiExpansion;
import com.github.salihbasicm.sedexlives.lang.LivesMessage;
import com.github.salihbasicm.sedexlives.listeners.PlayerDeath;
import com.github.salihbasicm.sedexlives.listeners.PlayerJoin;
import com.github.salihbasicm.sedexlives.listeners.PlayerQuit;
import com.github.salihbasicm.sedexlives.storage.FlatfileStorageProvider;
import com.github.salihbasicm.sedexlives.storage.LivesStorage;
import com.github.salihbasicm.sedexlives.storage.MySQLStorageProvider;
import com.github.salihbasicm.sedexlives.storage.StorageType;
import com.github.salihbasicm.sedexlives.util.LivesConfig;
import com.github.salihbasicm.sedexlives.util.LivesUserCache;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class SedexLives extends JavaPlugin {

    private LivesConfig livesConfig;
    private LivesUserCache livesUserCache;
    private LivesMessage livesMessage;

    private LivesStorage storage;

    private boolean papiHooked = false;

    private List<LivesUser> toggledOff;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        livesConfig = new LivesConfig(this.getConfig());

        livesMessage = new LivesMessage(this);
        livesMessage.saveDefaultMessages();

        initializeStorage(livesConfig.getStorageType());

        livesUserCache = new LivesUserCache();

        hookIntoPlaceholderAPI();

        registerListeners();

        LivesCommand livesCommand = new LivesCommand(this);
        Objects.requireNonNull(this.getServer().getPluginCommand("lives")).setExecutor(livesCommand);

        toggledOff = new ArrayList<>();

    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
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

    private void initializeStorage(final StorageType storageType) {

        switch (storageType) {

            case MYSQL:
                storage = new MySQLStorageProvider(this);
                break;
            case FLATFILE:
                storage = new FlatfileStorageProvider(this);
                break;
        }

    }

    /*
    Reloads the config and resets the livesConfig so it effectively gets reloaded too.
     */
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        livesConfig = new LivesConfig(this.getConfig());
    }

    /**
     * Sends debug messages to console if debug is true in the config.
     *
     * @param message Debug message
     */
    public void debugMessage(String message) {

        if (getLivesConfig().debugEnabled())
            this.getLogger().log(Level.INFO, "[DEBUG]: " + message);

    }

    /**
     * Plugin's {@link LivesConfig}.
     *
     * @return LivesConfig for this plugin's instance
     */
    public LivesConfig getLivesConfig() {
        return livesConfig;
    }

    /**
     * Represents all players that have toggled off the lives.
     *
     * @return HashMap of all players with toggled of lives
     */
    public List<LivesUser> getToggledOff() {
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

    public LoadingCache<LivesUser, Integer> getLivesUserCache() {
        return livesUserCache.getLivesCache();
    }

    public LivesMessage getLivesMessage() {
        return livesMessage;
    }

    public LivesStorage getStorage() {
        return storage;
    }

}
