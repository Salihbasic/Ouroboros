package com.github.salihbasicm.ouroboros.commands.sub;

import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.OuroborosUser;
import com.github.salihbasicm.ouroboros.commands.AbstractSubCommand;
import com.github.salihbasicm.ouroboros.lang.Message;
import com.github.salihbasicm.ouroboros.util.OuroborosPermissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleLivesCommand extends AbstractSubCommand {

    public ToggleLivesCommand(Ouroboros ouroboros) {
        super(ouroboros);
    }

    /*
    Defines the sub-command "toggle" used to toggle user's lives system.
    If their lives are toggled off, they will not lose any lives on death, but they will lose items and XP.

    Toggle is reset whenever player leaves the game.

    Usage: /lives toggle
     */

    @Override
    public String getHelp() {
        return formatHelp("/lives toggle",
                plugin.getOuroborosMessage().getSimpleMessage(Message.LIVES_TOGGLE_HELP));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("toggle")) {

            if (hasNoPermission(commandSender, OuroborosPermissions.TOGLE_LIVES)) {
                return true;
            }

            if (!(commandSender instanceof Player)) {
                invalidSenderMessage(commandSender);
                return true;
            }

            if (args.length > 1) {
                tooManyArguments(commandSender);
            }

            Player playerSender = (Player) commandSender;
            OuroborosUser user = new OuroborosUser(plugin, playerSender);

            if (user.isToggledOff()) {

                user.setToggledOff(false);
                user.getUser().sendMessage(plugin.getOuroborosMessage().getMessage(user, Message.LIVES_TOGGLE_ON));

            } else {

                user.setToggledOff(true);
                user.getUser().sendMessage(plugin.getOuroborosMessage().getMessage(user, Message.LIVES_TOGGLE_OFF));

            }

        }

        return true;
    }
}
