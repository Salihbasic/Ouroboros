package com.github.salihbasicm.ouroboros.storage.user;

import com.github.salihbasicm.ouroboros.OuroborosUser;
import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.storage.AbstractFlatFileStorage;

/**
 * Provides implementation of the plugin storage in forms of a simple YAML file.
 * This implementation is highly inefficient, as all forms of flatfile storage are,
 * especially for large servers with a large amount of users. Using a database is almost always
 * the better option.
 */
public class FlatfileUserStorageProvider extends AbstractFlatFileStorage implements OuroborosUserStorage {

    /*

    Format:

        <uuid>: <lives>

     */

    public FlatfileUserStorageProvider(final Ouroboros plugin, final String filename) {
        super(plugin, filename);
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
            plugin.getOuroborosCache().getUserCache().refresh(user);
        }

    }

    @Override
    public int getLives(final OuroborosUser user) {

        return (storageImpl.getString(user.getUniqueId().toString()) != null) ?
                storageImpl.getInt(user.getUniqueId().toString()) : -1;

    }
}
