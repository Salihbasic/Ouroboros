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

package com.github.salihbasicm.ouroboros.commands.sub;

import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.OuroborosUser;
import com.github.salihbasicm.ouroboros.commands.AbstractSubCommand;
import com.github.salihbasicm.ouroboros.lang.Message;
import com.github.salihbasicm.ouroboros.util.OuroborosPermissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLivesByNameCommand extends AbstractSubCommand {

    public SetLivesByNameCommand(Ouroboros ouroboros) {
        super(ouroboros);
    }

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
                plugin.getOuroborosMessage().getSimpleMessage(Message.LIVES_SET_HELP));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("set")) {

            if (hasNoPermission(commandSender, OuroborosPermissions.SET_LIVES)) {
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
            OuroborosUser targetUser = new OuroborosUser(plugin, target);

            final int value = Integer.parseInt(args[2]);

            if (value < 0) {
                commandSender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.LIVES_SET_NONNEGATIVE));
                return true;
            }

            if (targetUser.getUser() != null) { // Player exists

                if (override) { // Override is true

                    targetUser.updateLives(value);
                    successMessage(commandSender, targetUser);

                } else { // Override is false

                    if (!(value > targetUser.getMaxLives())) { // Value is less than or equal to max lives

                        targetUser.updateLives(value);
                        successMessage(commandSender, targetUser);

                    } else { // Value is greater than maxlives

                        commandSender.sendMessage(plugin.getOuroborosMessage()
                                                    .getSimpleMessage(Message.LIVES_SET_GREATERVALUE));

                    }

                }

            } else { // Player not found

                playerNotFound(commandSender);

            }

        }

        return true;
    }

    private void successMessage(CommandSender sender, OuroborosUser user) {
        sender.sendMessage(plugin.getOuroborosMessage().getMessage(user, Message.LIVES_SET_SUCCESS));
    }

}
