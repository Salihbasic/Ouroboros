package commands.sub;

import commands.AbstractSubCommand;
import database.SQLManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sedexlives.SedexLives;
import sedexlives.SedexLivesPermissions;

public class GetMaxlivesCommand extends AbstractSubCommand {

    private SedexLives plugin = SedexLives.getSedexLives();
    private SQLManager sqlManager = SQLManager.getSQLManager(plugin);

    /*
    Defines the sub-command "maxlives" used to get the maximum lives a player can have.

    Usage: /lives maxlives [player]

    Using player name can only get the online player.
     */

    @Override
    public String getHelp() {
        return ChatColor.RED + "/lives maxlives [player] " + ChatColor.WHITE + "- " + ChatColor.GREEN +
                "Attempts to get maximum lives of a player. Only works for online players.\n";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("maxlives")) {

            if (!hasPermission(commandSender, SedexLivesPermissions.CHECK_MAXLIVES)) {
                noPermissionMessage(commandSender);
                return true;
            }

            if (args.length == 1) { // Executed /lives maxlives

                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage("Only players can execute this command!");
                    return true;
                }

                final Player playerSender = (Player) commandSender;
                final int maxlives = getPlayerMaxLives(playerSender);

                playerSender.sendMessage(ChatColor.GREEN + "You have a maximum of " + maxlives + " lives.");

            }

            if (args.length == 2) { // Executed /lives maxlives <player>

                if (!hasPermission(commandSender, SedexLivesPermissions.CHECK_MAXLIVES_OTHERS)) {
                    noPermissionMessage(commandSender);
                    return true;
                }

                final String username = args[1];
                final Player target = plugin.getServer().getPlayer(username);

                if (target != null) { // Player found

                    final int maxlives = getPlayerMaxLives(target);
                    commandSender.sendMessage(target.getDisplayName() + ChatColor.GREEN +
                            "has a maximum of " + maxlives + " lives.");

                } else {

                    playerNotFound(commandSender, username);

                }

            }

            if (args.length > 3) {
                tooManyArguments(commandSender, getHelp());
                return true;
            }

        }

        return true;
    }
}
