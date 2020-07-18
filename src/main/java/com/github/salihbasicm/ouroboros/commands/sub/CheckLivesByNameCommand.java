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

import com.github.salihbasicm.ouroboros.OuroborosUser;
import com.github.salihbasicm.ouroboros.commands.AbstractSubCommand;
import com.github.salihbasicm.ouroboros.lang.Message;
import com.github.salihbasicm.ouroboros.lang.MessageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.util.OuroborosPermissions;

public class CheckLivesByNameCommand extends AbstractSubCommand {


    public CheckLivesByNameCommand(Ouroboros ouroboros) {
        super(ouroboros);
    }

    /*
    Defines the sub-command "check" used to get the value of lives.

    Usage: /lives check [player name]

    Using player name can only get the online player.
     */

    @Override
    public String getHelp() {
        return formatHelp("/lives check [player]",
                plugin.getOuroborosMessage().getSimpleMessage(Message.LIVES_CHECK_HELP));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("check")) {

            if (hasNoPermission(commandSender, OuroborosPermissions.CHECK_LIVES)) {
                return true;
            }

            if (args.length == 1) { // Executed /lives get

                if (!(commandSender instanceof Player)) {
                    invalidSenderMessage(commandSender);
                    return true;
                }

                final Player playerSender = (Player) commandSender;
                OuroborosUser user = new OuroborosUser(plugin, playerSender);

                user.sendMessage(Message.LIVES_MESSAGE, MessageType.FORMAT);

            }

            if (args.length == 2) { // Executed /lives get <player name>

                if (hasNoPermission(commandSender, OuroborosPermissions.CHECK_LIVES_OTHERS)) {
                    return true;
                }

                final Player target = plugin.getServer().getPlayerExact(args[1]);
                OuroborosUser targetUser = new OuroborosUser(plugin, target);

                if (targetUser.getUser() != null) {

                    targetUser.sendMessage(Message.LIVES_MESSAGE_OTHER, MessageType.FORMAT);

                } else {

                    playerNotFound(commandSender);

                }

            }

            if (args.length > 2) {
                tooManyArguments(commandSender);
                return true;
            }

        }

        return true;
    }
}
