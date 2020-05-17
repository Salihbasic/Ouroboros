package com.github.salihbasicm.sedexlives.commands.sub;

import com.github.salihbasicm.sedexlives.commands.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.salihbasicm.sedexlives.LivesUser;
import com.github.salihbasicm.sedexlives.SedexLives;
import com.github.salihbasicm.sedexlives.util.SedexLivesPermissions;

public class ToggleLivesCommand extends AbstractSubCommand {

    private SedexLives plugin = SedexLives.getSedexLives();

    /*
    Defines the sub-command "toggle" used to toggle user's lives system.
    If their lives are toggled off, they will not lose any lives on death, but they will lose items and XP.

    Toggle is reset whenever player leaves the game.

    Usage: /lives toggle
     */

    @Override
    public String getHelp() {
        return formatHelp("/lives toggle",
                "Toggles lives on or off. When toggled off, lives system will not affect you. " +
                        "Works only for a single session.");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("toggle")) {

            if (!hasPermission(commandSender, SedexLivesPermissions.TOGLE_LIVES)) {
                noPermissionMessage(commandSender);
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
                user.getUser().sendMessage(ChatColor.GREEN + "Your lives have been toggled on.");

            } else {

                user.setToggledOff(true);
                user.getUser().sendMessage(ChatColor.GREEN + "Your lives have been toggled off.");

            }

        }

        return true;
    }
}