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
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sedexlives.LivesUser;
import sedexlives.SedexLives;
import util.SedexLivesPermissions;

public class GetMaxlivesCommand extends AbstractSubCommand {

    private SedexLives plugin = SedexLives.getSedexLives();

    /*
    Defines the sub-command "maxlives" used to get the maximum lives a player can have.

    Usage: /lives maxlives [player]

    Using player name can only get the online player.
     */

    @Override
    public String getHelp() {
        return formatHelp("/lives maxlives [player]",
                "Attempts to get maximum lives of a player. Only works for online players.");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("maxlives")) {

            if (!hasPermission(commandSender, SedexLivesPermissions.CHECK_MAXLIVES)) {
                noPermissionMessage(commandSender);
                return true;
            }

            if (args.length == 1) { // Executed /lives maxlives

                if (!(commandSender instanceof Player)) {
                    invalidSenderMessage(commandSender);
                    return true;
                }

                final Player playerSender = (Player) commandSender;
                LivesUser user = new LivesUser(plugin, playerSender);

                playerSender.sendMessage(ChatColor.GREEN + "You have a maximum of " + user.getMaxLives() + " lives.");

            }

            if (args.length == 2) { // Executed /lives maxlives <player>

                if (!hasPermission(commandSender, SedexLivesPermissions.CHECK_MAXLIVES_OTHERS)) {
                    noPermissionMessage(commandSender);
                    return true;
                }

                final Player target = plugin.getServer().getPlayer(args[1]);
                LivesUser targetUser = new LivesUser(plugin, target);

                if (targetUser.getUser() != null) { // Player found

                    commandSender.sendMessage(targetUser.getUser().getName() + ChatColor.GREEN +
                            "has a maximum of " + targetUser.getMaxLives() + " lives.");

                } else {

                    playerNotFound(commandSender, args[1]);

                }

            }

            if (args.length > 3) {
                tooManyArguments(commandSender, getHelp());
                return true;
            }

        }

        return true;
    }
}
