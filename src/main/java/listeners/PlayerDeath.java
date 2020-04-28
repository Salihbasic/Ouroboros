package listeners;

import database.SQLManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import sedexlives.SedexLives;
import sedexlives.SedexLivesPermissions;

public class PlayerDeath implements Listener {

    private SedexLives plugin = SedexLives.getSedexLives();
    private SQLManager sqlManager = SQLManager.getSQLManager(plugin);

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        final String uuid = player.getUniqueId().toString();

        if (player.hasPermission(SedexLivesPermissions.USE_LIVES)) { // Without the permission nothing happens

            int lives = sqlManager.getPlayerLives(uuid);

            if (lives > 0) {
                event.setKeepInventory(true);

                if (player.hasPermission(SedexLivesPermissions.KEEP_EXP))
                    event.setKeepLevel(true);

                // Drops need to be cleared
                event.getDrops().clear();

                // Same
                if (player.hasPermission(SedexLivesPermissions.KEEP_EXP))
                    event.setDroppedExp(0);

                int newLives = lives - 1;

                // Attempts to update (async) table with new lives by taking the old value and subtracting it with 1
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        final String sql = "UPDATE sl_lives SET lives = " + newLives + " WHERE uuid='" + uuid + "';";

                        sqlManager.update(sql);
                    }

                }.runTaskAsynchronously(plugin);
            }

            if (lives == -1) {
                plugin.getLogger().severe("Error occurred while retrieving lives.");
            }

        }
    }

}
