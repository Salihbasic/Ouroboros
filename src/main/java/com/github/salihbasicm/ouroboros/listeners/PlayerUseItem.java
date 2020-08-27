package com.github.salihbasicm.ouroboros.listeners;

import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.OuroborosUser;
import com.github.salihbasicm.ouroboros.util.OuroborosItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerUseItem implements Listener {

    private final Ouroboros plugin;

    public PlayerUseItem(final Ouroboros plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent event) {
        final OuroborosUser user = new OuroborosUser(plugin, event.getPlayer());

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (event.getItem() == null)
                return;

            if (event.getItem().getType() == Material.AIR)
                return;

            final ItemStack eventItem = event.getItem();

            if (OuroborosItem.isOuroborosItem(plugin, eventItem))
                user.useOuroborosItem(eventItem);
        }

    }

}
