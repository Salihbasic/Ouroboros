package com.github.salihbasicm.ouroboros.storage;

import com.github.salihbasicm.ouroboros.OuroborosUser;
import com.github.salihbasicm.ouroboros.Ouroboros;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Provides implementation of the plugin storage in forms of a simple YAML file.
 * This implementation is highly inefficient, as all forms of flatfile storage are,
 * especially for large servers with a large amount of users. Using a database is almost always
 * the better option.
 */
public class FlatfileStorageProvider implements OuroborosStorage {

    /*

    Format:

        <uuid>: <lives>

     */

    private final Ouroboros plugin;

    private static final String FILENAME = "data.yml";

    private File storageFile;
    private FileConfiguration storageImpl;

    public FlatfileStorageProvider(final Ouroboros plugin) {
        this.plugin = plugin;

        storageImpl = loadStorage();
        plugin.getLogger().info("Data storage set to: " + this.getClass().getSimpleName());
    }

    @Override
    public void createUser(final OuroborosUser user, final int defaultLives) {

        if (storageImpl.getString(user.getUniqueId().toString()) == null) {
            storageImpl.set(user.getUniqueId().toString(), defaultLives);
            saveStorage();
        }

    }

    @Override
    public void updateLives(final OuroborosUser user, final int newValue) {

        if (storageImpl.getString(user.getUniqueId().toString()) != null) {
            storageImpl.set(user.getUniqueId().toString(), newValue);
            saveStorage();
            plugin.getOuroborosUserCache().refresh(user);
        }

    }

    @Override
    public int getLives(final OuroborosUser user) {

        return (storageImpl.getString(user.getUniqueId().toString()) != null) ?
                storageImpl.getInt(user.getUniqueId().toString()) : -1;

    }

    /**
     * Loads the storage from a YAML file.
     * @return {@link FileConfiguration} with storage files
     */
    private FileConfiguration loadStorage() {

        createStorage();
        return YamlConfiguration.loadConfiguration(storageFile);

    }

    /**
     * Writes new data to the storage file.
     * Reloads the file afterwards.
     */
    private void saveStorage() {

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
    private void createStorage() {

        if (storageFile == null)
            storageFile = new File(plugin.getDataFolder(), FILENAME);

        try {

            boolean created = storageFile.createNewFile();

            if (created)
                plugin.getLogger().info("Created new storage file " + FILENAME);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Reloads the storage.
     */
    private void reloadStorage() {
        storageImpl = loadStorage();
    }
}
