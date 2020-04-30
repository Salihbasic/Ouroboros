package listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import sedexlives.SedexLives;

public class PlayerQuit implements Listener {

    SedexLives plugin = SedexLives.getSedexLives();

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        plugin.getToggledOff().remove(player);
    }

}
