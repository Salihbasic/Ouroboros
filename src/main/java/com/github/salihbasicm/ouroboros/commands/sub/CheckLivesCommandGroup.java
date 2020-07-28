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
import com.github.salihbasicm.ouroboros.commands.AbstractCommandGroup;
import com.github.salihbasicm.ouroboros.commands.processor.SenderType;
import com.github.salihbasicm.ouroboros.commands.processor.SubCommand;
import com.github.salihbasicm.ouroboros.lang.Message;
import com.github.salihbasicm.ouroboros.lang.MessageType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.util.OuroborosPermissions;

public class CheckLivesCommandGroup extends AbstractCommandGroup {

    public CheckLivesCommandGroup(Ouroboros ouroboros) {
        super(ouroboros);
    }

    /*
    Group: check
    Description: Checks user lives
    Provides sub-commands:
        * self - checks lives of the sender
        * player <player> - checks lives of specified player
     */

    @SubCommand(
            sub = "self",
            usage = "/lives check self",
            help = "Checks your own lives.",
            permission = OuroborosPermissions.CHECK_LIVES,
            sender = SenderType.PLAYER
    )
    private void livesCheckYourself(final CommandSender sender, final String[] args) {
        final OuroborosUser user = new OuroborosUser(plugin, (Player) sender);
        user.sendMessage(Message.LIVES_MESSAGE, MessageType.FORMAT);
    }

    @SubCommand(
            sub = "player",
            reqArgs = 1,
            usage = "/lives check player <player>",
            help = "Checks the lives of the specified player.",
            permission = OuroborosPermissions.CHECK_LIVES_OTHERS
    )
    private void livesCheckOthers(final CommandSender sender, final String[] args) {
        final Player target = plugin.getServer().getPlayerExact(args[0]);
        if (target != null) {
            final OuroborosUser targetUser = new OuroborosUser(plugin, target);
            sender.sendMessage(plugin.getOuroborosMessage().getMessage(targetUser, Message.LIVES_MESSAGE_OTHER));
        } else {
            playerNotFound(sender);
        }
    }
}
