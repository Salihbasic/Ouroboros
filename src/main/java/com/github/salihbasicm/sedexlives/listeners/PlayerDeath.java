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

package com.github.salihbasicm.sedexlives.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import com.github.salihbasicm.sedexlives.LivesUser;
import com.github.salihbasicm.sedexlives.SedexLives;
import com.github.salihbasicm.sedexlives.util.SedexLivesPermissions;

public class PlayerDeath implements Listener {

    private final SedexLives plugin;

    public PlayerDeath(SedexLives plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        LivesUser livesUser = new LivesUser(plugin, event.getEntity());

        if (livesUser.getUser().hasPermission(SedexLivesPermissions.USE_LIVES) &&
                !livesUser.isToggledOff()) {

            int lives = livesUser.getLives();

            if (lives > 0) {

                event.setKeepInventory(true);

                if (livesUser.getUser().hasPermission(SedexLivesPermissions.KEEP_EXP))
                    event.setKeepLevel(true);

                // Drops need to be cleared
                event.getDrops().clear();

                // Same
                if (livesUser.getUser().hasPermission(SedexLivesPermissions.KEEP_EXP))
                    event.setDroppedExp(0);

                // Attempts to update (async) table with new lives by taking the old value and subtracting it with 1
                livesUser.updateLives(lives - 1);
            }

            if (lives == -1) {
                plugin.getLogger().severe("Error occurred while retrieving lives.");
            }

        }
    }

}
