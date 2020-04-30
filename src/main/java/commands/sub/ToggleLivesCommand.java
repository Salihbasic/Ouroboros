package commands.sub;

import commands.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sedexlives.SedexLives;
import sedexlives.SedexLivesPermissions;

public class ToggleLivesCommand extends AbstractSubCommand {

    private SedexLives plugin = SedexLives.getSedexLives();

    @Override
    public String getHelp() {
        return ChatColor.RED + "/lives toggle " + ChatColor.WHITE + "- " + ChatColor.GREEN +
                "Toggles lives on or off. When toggled off, lives system will not affect you. " +
                "Works only for a single session.\n";
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

            if (plugin.getToggledOff().contains(playerSender)) {

                plugin.getToggledOff().remove(playerSender);
                playerSender.sendMessage(ChatColor.GREEN + "Your lives have been toggled on.");

            } else {

                plugin.getToggledOff().add(playerSender);
                playerSender.sendMessage(ChatColor.GREEN + "Your lives have been toggled off.");

            }

        }

        return true;
    }
}
