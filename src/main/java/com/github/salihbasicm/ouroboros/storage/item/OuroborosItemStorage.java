package com.github.salihbasicm.ouroboros.storage.item;

import org.bukkit.inventory.ItemStack;

import java.util.Set;

public interface OuroborosItemStorage {

    /**
     * Returns the specified Ouroboros item based on its internal name.
     * @param internalName Internal item name
     * @return OuroborosItem if the item is found, {@code null} otherwise
     */
    ItemStack getItem(final String internalName);

    /**
     * Returns all registered Ouroboros items by their internal name.
     * @return Set of all internal item names
     */
    Set<String> getRegisteredItems();

    /**
     * Saves the item the player holds in their main hand with all its qualities, along with provided parameters.
     * @param item Item in user's main hand to be saved
     * @param internalName Item's internal name
     */
    void saveItem(final ItemStack item, final String internalName);

    /**
     * Removes the specified item from the storage. Fails if the item does not exist.
     * @param internalName Internal item name
     */
    void removeItem(final String internalName);

}
