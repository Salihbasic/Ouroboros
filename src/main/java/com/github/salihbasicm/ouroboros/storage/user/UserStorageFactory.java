package com.github.salihbasicm.ouroboros.storage.user;

import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.storage.AbstractStorageFactory;
import com.github.salihbasicm.ouroboros.storage.StorageType;

public class UserStorageFactory implements AbstractStorageFactory<OuroborosUserStorage> {

    private final Ouroboros plugin;

    public UserStorageFactory(final Ouroboros plugin) {
        this.plugin = plugin;
    }

    @Override
    public OuroborosUserStorage getStorage(StorageType storageType) {
        switch (storageType) {
            case MYSQL:
                return new MySQLUserStorageProvider(plugin);
            default:
                return new FlatfileUserStorageProvider(plugin, "data.yml");
        }
    }
}
