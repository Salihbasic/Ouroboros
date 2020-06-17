package com.github.salihbasicm.ouroboros.listeners;

import com.github.salihbasicm.ouroboros.Ouroboros;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import com.github.salihbasicm.ouroboros.OuroborosUser;

public class PlayerQuit implements Listener {

    private final Ouroboros plugin;

    public PlayerQuit(Ouroboros plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        OuroborosUser user = new OuroborosUser(plugin, event.getPlayer());

        if (user.isToggledOff())
            user.setToggledOff(false);
    }

}
