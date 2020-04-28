package commands.sub;

import commands.AbstractSubCommand;
import database.SQLManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sedexlives.SedexLives;
import sedexlives.SedexLivesPermissions;

public class CheckLivesByNameCommand extends AbstractSubCommand {

    private SedexLives plugin = SedexLives.getSedexLives();
    private SQLManager sqlManager = SQLManager.getSQLManager(plugin);

    /*
    Defines the sub-command "get" used to get the value of lives.

    Usage: /lives get [player name]

    Using player name can only get the online player.
     */

    @Override
    public String getHelp() {
        return ChatColor.RED + "/lives get [player] " + ChatColor.WHITE + "- " + ChatColor.GREEN +
                "Attempts to get lives of a player. Only works for online players.\n";
    }

    public static final String CHECK_MAXLIVES = "sedexlives.check.maxlives";
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("check")) {

            if (!hasPermission(commandSender, SedexLivesPermissions.CHECK_LIVES)) {
                noPermissionMessage(commandSender);
                return true;
            }

            if (args.length == 1) { // Executed /lives get

                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage("Only players can execute this command!");
                    return true;
                }

                final Player playerSender = (Player) commandSender;
                final String senderUUID = playerSender.getUniqueId().toString();

                int lives = sqlManager.getPlayerLives(senderUUID);
                playerSender.sendMessage(plugin.getConfigManager().getLivesMessage(plugin.isPapiHooked(),
                        playerSender, lives));

            }

            if (args.length == 2) { // Executed /lives get <player name>

                if (!hasPermission(commandSender, SedexLivesPermissions.CHECK_LIVES_OTHERS)) {
                    noPermissionMessage(commandSender);
                    return true;
                }

                final String username = args[1];
                final Player target = plugin.getServer().getPlayerExact(username);

                if (target != null) { // Player found

                    final String targetUUID = target.getUniqueId().toString();

                    int lives = sqlManager.getPlayerLives(targetUUID);
                    commandSender.sendMessage(plugin.getConfigManager().getLivesMessage(plugin.isPapiHooked(),
                            target, lives));

                } else {

                    playerNotFound(commandSender, username);

                }

            }

            if (args.length > 2) {
                tooManyArguments(commandSender, getHelp());
                return true;
            }

        }

        return true;
    }
}
