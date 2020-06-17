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

package com.github.salihbasicm.ouroboros.commands;

import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.lang.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Represents an abstract Ouroboros sub-command.
 */
public abstract class AbstractSubCommand implements CommandExecutor  {

    protected Ouroboros plugin;

    public AbstractSubCommand(Ouroboros plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns the help message of the command. Help message ought to be formatted uniformly, since
     * the default help command also uses this method to fetch help for each command.
     *
     * @return Command help message
     */
    public abstract String getHelp();

    /**
     * Formats the command help message according to the standard formatting for this plugin.
     *
     * @param command Command (format: {@code /lives <subcommand> <args>})
     * @param description Command description
     * @return Formatted help message
     */
    public String formatHelp(String command, String description) {
        return ChatColor.RED + command + ChatColor.WHITE + " - " + ChatColor.GREEN + description + "\n";
    }

    @Override
    public abstract boolean onCommand(CommandSender commandSender, Command command, String label, String[] args);

    /*
    Additional abstraction to check for permissions.
     */
    public boolean hasNoPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            noPermissionMessage(sender);
            return true;
        }

        return false;
    }

    /*
    Message sent if there is no permission found.
     */
    public void noPermissionMessage(CommandSender sender) {
        sender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.NO_PERMISSION));
    }

    /*
    Message sent if the player is not found.
     */
    public void playerNotFound(CommandSender sender) {
        sender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.USER_NOT_FOUND));
    }

    /*
    Message sent if the command does not have enough provided arguments.
     */
    public void notEnoughArguments(CommandSender sender, String help) {
        sender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.NOT_ENOUGH_ARGUMENTS));
        sender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.CORRECT_USAGE) + "\n" + getHelp());
    }

    /*
    Message sent if the command has too many provided arguments.
     */
    public void tooManyArguments(CommandSender sender, String help) {
        sender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.TOO_MANY_ARGUMENTS));
        sender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.CORRECT_USAGE) + "\n" + getHelp());
    }

    /*
    Message sent if the player is not supposed to execute the command.
     */
    public void invalidSenderMessage(CommandSender sender) {
        sender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.INVALID_SENDER));
    }

}
