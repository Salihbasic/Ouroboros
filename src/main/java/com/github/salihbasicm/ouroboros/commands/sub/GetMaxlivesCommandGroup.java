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
import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.commands.AbstractCommandGroup;
import com.github.salihbasicm.ouroboros.commands.processor.SenderType;
import com.github.salihbasicm.ouroboros.commands.processor.SubCommand;
import com.github.salihbasicm.ouroboros.lang.Message;
import com.github.salihbasicm.ouroboros.lang.MessageType;
import com.github.salihbasicm.ouroboros.util.OuroborosPermissions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetMaxlivesCommandGroup extends AbstractCommandGroup {

    public GetMaxlivesCommandGroup(Ouroboros ouroboros) {
        super(ouroboros);
    }

    /*
    Group: maxlives
    Description: Checks user's maximum amount of lives
    Provides sub-commands:
        * self - checks maxlives of the sender
        * player <player> - checks maxlives of specified player
    */


    @SubCommand(
            sub = "self",
            usage = "/lives maxlives self",
            help = "Checks your own maximum lives.",
            permission = OuroborosPermissions.CHECK_MAXLIVES,
            sender = SenderType.PLAYER
    )
    private void maxlivesCheckYourself(final CommandSender sender, final String[] args) {
        final OuroborosUser user = new OuroborosUser(plugin, (Player) sender);
        user.sendMessage(Message.LIVES_MAXIMUM, MessageType.FORMAT);
    }

    @SubCommand(
            sub = "player",
            reqArgs = 1,
            usage = "/lives maxlives player <player>",
            help = "Checks the maximum lives of the specified players.",
            permission = OuroborosPermissions.CHECK_MAXLIVES_OTHERS
    )
    private void maxlivesCheckOthers(final CommandSender sender, final String[] args) {
        final Player target = Bukkit.getServer().getPlayerExact(args[0]);
        if (target != null) {
            final OuroborosUser targetUser = new OuroborosUser(plugin, target);
            sender.sendMessage(plugin.getOuroborosMessage().getMessage(targetUser, Message.LIVES_MAXIMUM_OTHER));
        }
    }
}
