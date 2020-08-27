package com.github.salihbasicm.ouroboros.storage.item;

import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.storage.AbstractStorageFactory;
import com.github.salihbasicm.ouroboros.storage.StorageType;

public class ItemStorageFactory implements AbstractStorageFactory<OuroborosItemStorage> {

    private final Ouroboros plugin;

    public ItemStorageFactory(final Ouroboros plugin) {
        this.plugin = plugin;
    }

    @Override
    public OuroborosItemStorage getStorage(StorageType storageType) {
        switch (storageType) {
            default:
                return new FlatfileItemStorageProvider(plugin, "items.yml");
        }
    }
}
