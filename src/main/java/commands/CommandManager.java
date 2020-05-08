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

package commands;

import commands.sub.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import util.SedexLivesPermissions;

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
        subCommands.put("toggle", new ToggleLivesCommand());
    }


    @Override
    public String getHelp() {

        return ChatColor.GOLD + "SedexLives Help:\n" +
                subCommands.get("reload").getHelp() +
                subCommands.get("info").getHelp() +
                subCommands.get("check").getHelp() +
                subCommands.get("maxlives").getHelp() +
                subCommands.get("toggle").getHelp() +
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
