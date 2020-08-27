package com.github.salihbasicm.ouroboros.commands.sub;

import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.OuroborosUser;
import com.github.salihbasicm.ouroboros.commands.AbstractCommandGroup;
import com.github.salihbasicm.ouroboros.commands.processor.SenderType;
import com.github.salihbasicm.ouroboros.commands.processor.SubCommand;
import com.github.salihbasicm.ouroboros.messages.Message;
import com.github.salihbasicm.ouroboros.util.OuroborosPermissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleLivesCommandGroup extends AbstractCommandGroup {

    public ToggleLivesCommandGroup(Ouroboros ouroboros) {
        super(ouroboros);
    }

    /*
    Group: toggle
    Description: Toggles user lives
    Provides sub-commands:
        * self - toggles lives of the sender
        * player <player> - toggles lives of specified player
     */

    @SubCommand(
            sub = "self",
            usage = "/lives toggle self",
            help = "Toggles your lives on or off.",
            permission = OuroborosPermissions.TOGLE_LIVES,
            sender = SenderType.PLAYER
    )
    private void livesToggleYourself(final CommandSender sender, final String[] args) {
        final OuroborosUser user = new OuroborosUser(plugin, (Player) sender);

        if (user.isToggledOff()) {

            user.setToggledOff(false);
            sender.sendMessage(Message.LIVES_TOGGLE.formatMessage("on"));

        } else {

            user.setToggledOff(true);
            sender.sendMessage(Message.LIVES_TOGGLE.formatMessage("off"));

        }
    }

    @SubCommand(
            sub = "player",
            reqArgs = 1,
            usage = "/lives toggle player <player>",
            help = "Toggles specified player's lives on or off.",
            permission = OuroborosPermissions.TOGLE_LIVES_OTHERS
    )
    private void livesToggleOthers(final CommandSender sender, final String[] args) {
        final Player target = plugin.getServer().getPlayerExact(args[0]);

        if (target != null) {
            final OuroborosUser targetUser = new OuroborosUser(plugin, target);

            if (targetUser.isToggledOff()) {

                targetUser.setToggledOff(false);
                sender.sendMessage(Message.LIVES_TOGGLE_OTHER.formatMessage(target.getDisplayName(), "on"));

            } else {

                targetUser.setToggledOff(true);
                sender.sendMessage(Message.LIVES_TOGGLE_OTHER.formatMessage(target.getDisplayName(), "off"));

            }

        } else { // target == null

            playerNotFound(sender, args[0]);

        }
    }
}
