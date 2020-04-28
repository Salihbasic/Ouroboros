package commands.sub;

import commands.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import sedexlives.SedexLives;
import sedexlives.SedexLivesPermissions;

public class ReloadLivesCommand extends AbstractSubCommand {

    private SedexLives plugin = SedexLives.getSedexLives();

    /*
    Defines the sub-command "reload" used to reload the config.

    Usage: /lives reload
     */

    @Override
    public String getHelp() {
        return ChatColor.RED + "/lives reload " + ChatColor.WHITE + "- " + ChatColor.GREEN + "Reloads the config.\n";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("reload")) {

            if (!hasPermission(commandSender, SedexLivesPermissions.RELOAD_CONFIG)) {
                noPermissionMessage(commandSender);
                return true;
            }

            if (args.length > 1) {
                tooManyArguments(commandSender, getHelp());
                return true;
            }

            commandSender.sendMessage(ChatColor.RED + "Attempting to reload config.");
            plugin.reloadConfig();
            commandSender.sendMessage(ChatColor.RED + "Successfully reloaded config!");

        }

        return true;
    }
}
