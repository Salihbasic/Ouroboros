package com.github.salihbasicm.ouroboros.util;

import com.github.salihbasicm.ouroboros.Ouroboros;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public final class OuroborosItem {

    private OuroborosItem() {}

    private static final String IDENTIFIER = "ouroboros-item";
    private static final String ADD = "ouroboros-item-add";
    private static final String OVERRIDE = "ouroboros-item-override";

    public static String serialize(final ItemStack item) {
        final YamlConfiguration config = new YamlConfiguration();
        config.set("item", item.serialize());
        return config.saveToString();
    }

    public static ItemStack deserialize(final String serializedItem) {
        final YamlConfiguration config = new YamlConfiguration();

        try {
            config.loadFromString(serializedItem);
            return config.getItemStack(serializedItem + "item");
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack createOuroborosItem(final Ouroboros plugin, final int add,
                                                final boolean override, final ItemStack item) {

        final ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Expected meta but got null!");
        final PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

        dataContainer.set(new NamespacedKey(plugin, IDENTIFIER), PersistentDataType.INTEGER, 1);
        dataContainer.set(new NamespacedKey(plugin, ADD), PersistentDataType.INTEGER, Math.abs(add));
        dataContainer.set(new NamespacedKey(plugin, OVERRIDE), PersistentDataType.INTEGER, override ? 1 : 0);

        item.setItemMeta(meta);

        return item;
    }

    public static int getAddFromOuroborosItem(final Ouroboros plugin, final ItemStack item) {

        final ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Expected meta but got null!");
        final PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        final NamespacedKey addKey = new NamespacedKey(plugin, ADD);

        if (!isOuroborosItem(plugin, item))
            return -1;

        if (dataContainer.has(addKey, PersistentDataType.INTEGER))
            return dataContainer.get(addKey, PersistentDataType.INTEGER);
        else
            return -1;
    }

    public static boolean getOverrideFromOuroborosItem(final Ouroboros plugin, final ItemStack item) {

        final ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Expected meta but got null!");
        final PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        final NamespacedKey overrideKey = new NamespacedKey(plugin, OVERRIDE);

        if (!isOuroborosItem(plugin, item))
            return false;

        if (dataContainer.has(overrideKey, PersistentDataType.INTEGER))
            return dataContainer.get(overrideKey, PersistentDataType.INTEGER) > 0;
        else
            return false;

    }

    public static boolean isOuroborosItem(final Ouroboros plugin, final ItemStack item) {
        final ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Expected meta but got null!");
        final PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

        final NamespacedKey identKey = new NamespacedKey(plugin, IDENTIFIER);

        return dataContainer.has(identKey, PersistentDataType.INTEGER);
    }
}
