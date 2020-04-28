package listeners;

import database.SQLManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import sedexlives.SedexLives;
import sedexlives.SedexLivesPermissions;

public class PlayerJoin implements Listener {

    private SedexLives plugin = SedexLives.getSedexLives();
    private SQLManager sqlManager = SQLManager.getSQLManager(plugin);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String uuid = player.getUniqueId().toString();

        /*
        This runs every time the player joins. It will open an async task and attempt to insert a player.

        UUID has to be unique, so if the player already exists (i.e has UUID) in the database, nothing happens.
        If the player is not yet in the database, he gets in.
         */

        if (player.hasPermission(SedexLivesPermissions.USE_LIVES)) {

            int defaultValue = plugin.getConfigManager().getDefaultLives();

            new BukkitRunnable() {

                @Override
                public void run() {
                    // If the UUID is already in the database nothing will happen, since UUID has the UNIQUE constraint.
                    final String update = "INSERT IGNORE INTO sl_lives(lives, uuid) VALUES (" + defaultValue +
                            ", '" + uuid + "');";
                    sqlManager.update(update);
                }

            }.runTaskAsynchronously(plugin);

        }
    }

}
