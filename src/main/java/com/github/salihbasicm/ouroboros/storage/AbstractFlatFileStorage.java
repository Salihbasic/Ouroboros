package com.github.salihbasicm.ouroboros.storage;

import com.github.salihbasicm.ouroboros.Ouroboros;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class AbstractFlatFileStorage {

    protected final Ouroboros plugin;

    protected File storageFile;
    protected FileConfiguration storageImpl;

    protected final String filename;

    protected AbstractFlatFileStorage(final Ouroboros plugin, final String filename) {
        this.plugin = plugin;
        this.filename = filename;

        createStorage();
        this.storageImpl = loadStorage();
    }

    /**
     * Loads the storage from a YAML file.
     * @return {@link FileConfiguration} with storage files
     */
    protected FileConfiguration loadStorage() {

        createStorage();
        return YamlConfiguration.loadConfiguration(storageFile);

    }

    /**
     * Writes new data to the storage file.
     * Reloads the file afterwards.
     */
    protected void saveStorage() {

        if (storageFile != null && storageImpl != null) {
            try {
                storageImpl.save(storageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        reloadStorage();

    }

    /**
     * Creates a new storage file if and only if it does not already exist.
     */
    protected void createStorage() {

        if (storageFile == null)
            storageFile = new File(plugin.getDataFolder(), filename);

        try {

            boolean created = storageFile.createNewFile();

            if (created)
                plugin.getLogger().info("Created new storage file " + filename);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Reloads the storage.
     */
    protected void reloadStorage() {
        storageImpl = loadStorage();
    }

}
