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

package com.github.salihbasicm.sedexlives.commands.sub;

import com.github.salihbasicm.sedexlives.commands.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.salihbasicm.sedexlives.LivesUser;
import com.github.salihbasicm.sedexlives.SedexLives;
import com.github.salihbasicm.sedexlives.util.SedexLivesPermissions;

public class SetLivesByNameCommand extends AbstractSubCommand {

    private SedexLives plugin = SedexLives.getSedexLives();

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
        return formatHelp("/lives set <player> <value> [override]",
                "Attempts to set lives of a player where value is amount of lives. If override is true it will " +
                        "override any maxlives permissions. Only works for online players.");
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

            Player target = plugin.getServer().getPlayer(args[1]);
            LivesUser targetUser = new LivesUser(plugin, target);

            final int value = Integer.parseInt(args[2]);

            if (value < 0) {
                commandSender.sendMessage(ChatColor.RED + "Value must not be negative!");
                return true;
            }

            if (targetUser.getUser() != null) { // Player exists

                if (override) { // Override is true

                    targetUser.updateLives(value);
                    successMessage(commandSender, targetUser.getUser().getName(), value);

                } else { // Override is false

                    if (!(value > targetUser.getMaxLives())) { // Value is less than or equal to max lives

                        targetUser.updateLives(value);
                        successMessage(commandSender, targetUser.getUser().getName(), value);

                    } else { // Value is greater than maxlives

                        commandSender.sendMessage(ChatColor.RED + "Value is greater than player's max lives.");

                    }

                }

            } else { // Player not found

                playerNotFound(commandSender, args[1]);

            }

        }

        return true;
    }

    private void successMessage(CommandSender sender, String playerName, final int lives) {
        sender.sendMessage(ChatColor.GREEN + "Successfully set lives of " + playerName + " to " + lives + ".");
    }

}
