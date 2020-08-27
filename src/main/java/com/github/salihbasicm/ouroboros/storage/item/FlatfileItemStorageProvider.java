package com.github.salihbasicm.ouroboros.storage.item;

import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.storage.AbstractFlatFileStorage;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class FlatfileItemStorageProvider extends AbstractFlatFileStorage implements OuroborosItemStorage {

    /*

    Format:

        <internal name>: <serialized string>

     */

    public FlatfileItemStorageProvider(final Ouroboros plugin, final String filename) {
        super(plugin, filename);
    }

    @Override
    public ItemStack getItem(String internalName) {
        return storageImpl.getItemStack(internalName);
    }

    @Override
    public Set<String> getRegisteredItems() {
        return storageImpl.getKeys(false);
    }

    @Override
    public void saveItem(ItemStack item, String internalName) {
        storageImpl.set(internalName, item);
        saveStorage();
    }

    @Override
    public void removeItem(String internalName) {
        storageImpl.set(internalName, null);
        saveStorage();
    }
}
