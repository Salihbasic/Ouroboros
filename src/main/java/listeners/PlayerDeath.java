/*
MIT License

Copyright (c) 2020 Steinein_

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

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

        if (player.hasPermission(SedexLivesPermissions.USE_LIVES) &&
                !player.hasPermission(SedexLivesPermissions.TOGGLE_OFF_LIVES)) { // Without the permission nothing happens

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
