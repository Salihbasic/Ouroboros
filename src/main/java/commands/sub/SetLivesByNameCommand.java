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

package commands.sub;

import commands.AbstractSubCommand;
import database.SQLManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sedexlives.SedexLives;
import sedexlives.SedexLivesPermissions;

public class SetLivesByNameCommand extends AbstractSubCommand {

    private SedexLives plugin = SedexLives.getSedexLives();
    private SQLManager sqlManager = SQLManager.getSQLManager(plugin);

    /*
    Defines the sub-command "set" used to set the value of lives.

    Usage: /lives set <player name> <value> [override].

    Using player name can only get the online player.

    Value has to be a non-negative integer.

    Override can be either true or false. It is true by default.
    If true, it will override the maxlives permission.
    If false, it will check for maxlives and ignore the command if the value is higher than maxlives.

     */

    @Override
    public String getHelp() {
        return ChatColor.RED + "/lives set <player> <value> [override] " + ChatColor.WHITE + "- " + ChatColor.GREEN +
                "Attempts to set lives of a player where value is amount of lives. If override is true it will " +
                "override any maxlives permissions. Only works for online players.\n";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("set")) {

            if (!hasPermission(commandSender, SedexLivesPermissions.SET_LIVES)) {
                noPermissionMessage(commandSender);
                return true;
            }

            boolean override = true;

            if (args.length < 3) {
                notEnoughArguments(commandSender, getHelp());
                return true;
            }

            if (args.length == 4) {
                override = Boolean.parseBoolean(args[3]);
            }

            if (args.length > 4) {
                tooManyArguments(commandSender, getHelp());
                return true;
            }

            final String username = args[1];
            Player target = plugin.getServer().getPlayer(username);

            final int value = Integer.parseInt(args[2]);

            if (value < 0) {
                commandSender.sendMessage(ChatColor.RED + "Value must not be negative!");
                return true;
            }

            if (target != null) { // Player exists

                final String targetUUID = target.getUniqueId().toString();

                if (override) { // Override is true

                    updater(targetUUID, value).runTaskAsynchronously(plugin);

                } else { // Override is false

                    if (!(value > getPlayerMaxLives(target))) { // Value is less than or equal to max lives

                        updater(targetUUID, value).runTaskAsynchronously(plugin);

                    } else { // Value is greater than maxlives

                        commandSender.sendMessage(ChatColor.RED + "Value is greater than player's max lives.");

                    }

                }

            } else { // Player not found

                playerNotFound(commandSender, username);

            }

        }

        return true;
    }

    private BukkitRunnable updater(String uuid, int newLives) {
        return new BukkitRunnable() {

            @Override
            public void run() {
                final String sql = "UPDATE sl_lives SET lives = " + newLives + " WHERE uuid = '" + uuid + "';";

                sqlManager.update(sql);
            }

        };
    }

}
