package com.github.salihbasicm.sedexlives.commands.sub;

import com.github.salihbasicm.sedexlives.LivesUser;
import com.github.salihbasicm.sedexlives.SedexLives;
import com.github.salihbasicm.sedexlives.commands.AbstractSubCommand;
import com.github.salihbasicm.sedexlives.lang.Message;
import com.github.salihbasicm.sedexlives.util.SedexLivesPermissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleLivesCommand extends AbstractSubCommand {

    public ToggleLivesCommand(SedexLives lives) {
        super(lives);
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
                plugin.getMessageManager().getSimpleMessage(Message.LIVES_TOGGLE_HELP));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("toggle")) {

            if (hasNoPermission(commandSender, SedexLivesPermissions.TOGLE_LIVES)) {
                return true;
            }

            if (!(commandSender instanceof Player)) {
                invalidSenderMessage(commandSender);
                return true;
            }

            if (args.length > 1) {
                tooManyArguments(commandSender, getHelp());
            }

            Player playerSender = (Player) commandSender;
            LivesUser user = new LivesUser(plugin, playerSender);

            if (user.isToggledOff()) {

                user.setToggledOff(false);
                user.getUser().sendMessage(plugin.getMessageManager().getMessage(user, Message.LIVES_TOGGLE_ON));

            } else {

                user.setToggledOff(true);
                user.getUser().sendMessage(plugin.getMessageManager().getMessage(user, Message.LIVES_TOGGLE_OFF));

            }

        }

        return true;
    }
}
