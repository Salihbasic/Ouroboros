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
import com.github.salihbasicm.ouroboros.commands.AbstractCommandGroup;
import com.github.salihbasicm.ouroboros.commands.processor.SubCommand;
import com.github.salihbasicm.ouroboros.lang.Message;
import com.github.salihbasicm.ouroboros.util.OuroborosPermissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLivesCommandGroup extends AbstractCommandGroup {

    public SetLivesCommandGroup(Ouroboros ouroboros) {
        super(ouroboros);
    }

    /*
    Group: set
    Description: Sets user lives
    Provides sub-commands:
        * player <player> <value> - sets lives of the specified player WITHOUT overriding their maxlives
        * player <player> <value> <override> - sets lives of the specified player and overrides maxlives if set to true
     */

    @SubCommand(
            sub = "player",
            reqArgs = 2,
            usage = "/lives set player <player> <lives>",
            help = "Sets the lives of specified player.",
            permission = OuroborosPermissions.SET_LIVES
    )
    private void setLivesOtherNoOverride(final CommandSender sender, final String[] args) {
        final Player target = plugin.getServer().getPlayerExact(args[0]);
        final int value = Integer.parseInt(args[1]);

        if (value < 0) {
            sender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.LIVES_SET_NONNEGATIVE));
            return;
        }

        if (target != null) {
            final OuroborosUser targetUser = new OuroborosUser(plugin, target);

            if (!(value > targetUser.getMaxLives())) { // Value is less than or equal to max lives

                targetUser.updateLives(value);
                successMessage(sender, targetUser);

            } else { // Value is greater than maxlives

                sender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.LIVES_SET_GREATERVALUE));

            }
        }
    }

    @SubCommand(
            sub = "player",
            reqArgs = 3,
            usage = "/lives set player <player> <lives> <override>",
            help = "Sets the lives of a specified player and decides whether to override maximum lives.",
            permission = OuroborosPermissions.SET_LIVES
    )
    private void setLivesOtherWithOverride(final CommandSender sender, final String[] args) {
        final Player target = plugin.getServer().getPlayerExact(args[0]);
        final int value = Integer.parseInt(args[1]);
        final boolean override = Boolean.parseBoolean(args[2]);

        if (value < 0) {
            sender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.LIVES_SET_NONNEGATIVE));
            return;
        }

        if (target != null) {

            final OuroborosUser targetUser = new OuroborosUser(plugin, target);

            if (override) {

                targetUser.updateLives(value);
                successMessage(sender, targetUser);

            } else {

                if (!(value > targetUser.getMaxLives())) { // Value is less than or equal to max lives

                    targetUser.updateLives(value);
                    successMessage(sender, targetUser);

                } else { // Value is greater than maxlives

                    sender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.LIVES_SET_GREATERVALUE));

                }
            }

        }
    }

    private void successMessage(CommandSender sender, OuroborosUser user) {
        sender.sendMessage(plugin.getOuroborosMessage().getMessage(user, Message.LIVES_SET_SUCCESS));
    }

}
