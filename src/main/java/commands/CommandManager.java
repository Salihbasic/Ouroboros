package commands;

import commands.sub.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import sedexlives.SedexLivesPermissions;

import java.util.HashMap;

/**
 * Manages all sub-commands used by SedexLives.
 * Sub-command is any argument immediately following {@code /lives} command.
 *
 */
public class CommandManager extends AbstractSubCommand implements CommandExecutor {

    private HashMap<String, AbstractSubCommand> subCommands = new HashMap<>();

    public CommandManager() {
        subCommands.put("reload", new ReloadLivesCommand());
        subCommands.put("info", new PluginInfoCommand());
        subCommands.put("check", new CheckLivesByNameCommand());
        subCommands.put("maxlives", new GetMaxlivesCommand());
        subCommands.put("set", new SetLivesByNameCommand());
    }


    @Override
    public String getHelp() {

        return ChatColor.GOLD + "SedexLives Help:\n" +
                subCommands.get("reload").getHelp() +
                subCommands.get("info").getHelp() +
                subCommands.get("check").getHelp() +
                subCommands.get("maxlives").getHelp() +
                subCommands.get("set").getHelp();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("lives")) {

            if (!hasPermission(commandSender, SedexLivesPermissions.USE_LIVES)) {
                noPermissionMessage(commandSender);
                return true;
            }

            if (args.length == 0) { // Executed /lives. Sends help message.

                commandSender.sendMessage(getHelp());

            }

            if (args.length > 0) { // Executed /lives <arguments...>

                final String argument = args[0];

                if (argument.equalsIgnoreCase("help")) {

                    commandSender.sendMessage(getHelp());
                    return true;

                }

                if (subCommands.containsKey(argument)) {

                    /*
                    Calls the registered command.
                    What has to be noted here is that the first argument is passed as a label, while the args array
                    is the same.
                    Because of this, the arguments of the sub command will have the same array index as their
                    position.

                    For example: /lives get <player>

                    Here a call to args[0] returns "get" and a call to args[1] returns "<player>".
                     */
                    subCommands.get(argument).onCommand(commandSender, command, argument, args);

                } else {

                    commandSender.sendMessage(ChatColor.RED + "Unrecognised sub-command " + argument + ".");
                    commandSender.sendMessage(getHelp());

                }

            }

        }

        return true;
    }
}
