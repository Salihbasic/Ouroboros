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
import com.github.salihbasicm.ouroboros.commands.processor.CommandProcessor;
import com.github.salihbasicm.ouroboros.commands.sub.*;
import com.github.salihbasicm.ouroboros.messages.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;

/**
 * <p>An Ouroboros command is the command of form: {@code /lives <group> <sub-command> [args]}.</p>
 *
 * <p>Every group does one specific thing and contains sub-commands doing that same thing
 * in a specific way. Every group is a child of the {@link AbstractCommandGroup} class.</p>
 *
 * <p>Each sub-command has its own label and required number of arguments. The label is denoted as
 * <sub-command> in the form above.</p>
 *
 * <p>When a sender attempts to execute an Ouroboros command, the plugin analyzes the provided input and attempts to
 * match it to a specific sub-command within a specific group, finally executing the sub-command if the match was
 * successful.</p>
 */
public class OuroborosCommand implements CommandExecutor {

    private final Ouroboros plugin;

    private final HashMap<String, AbstractCommandGroup> subCommands = new HashMap<>();

    public OuroborosCommand(final Ouroboros plugin) {
        this.plugin = plugin;

        subCommands.put("reload", new ReloadLivesCommandGroup(plugin));
        subCommands.put("check", new CheckLivesCommandGroup(plugin));
        subCommands.put("maxlives", new GetMaxlivesCommandGroup(plugin));
        subCommands.put("set", new SetLivesCommandGroup(plugin));
        subCommands.put("toggle", new ToggleLivesCommandGroup(plugin));
        subCommands.put("item", new ItemCommandGroup(plugin));
    }

    /**
     * Sends the sender all help messages within a specific command group.
     * @param group Command group
     * @param sender Sender requesting help
     */
    private void getHelp(final String group, final CommandSender sender) {
        if (subCommands.containsKey(group)) {
            final Class<? extends AbstractCommandGroup> subCommandClass = subCommands.get(group).getClass();
            final CommandProcessor processor = new CommandProcessor(plugin, subCommandClass);

            sender.sendMessage(Message.HELP_FOR_GROUP.formatMessage(group));
            processor.getSubCommandHelp().forEach(sender::sendMessage);
        } else {
            sender.sendMessage(Message.HELP_NOT_FOUND.formatMessage(group));
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        this.plugin.debugMessage("Attempting to execute command: " + command.toString());

        if (label.equalsIgnoreCase("lives")) {

            // Could implement the help command as a standalone command in the future
            if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
                commandSender.sendMessage(ChatColor.GREEN + "Use /lives help <group> to get command help.");
                commandSender.sendMessage(ChatColor.GREEN + "Groups: " +
                        ChatColor.RED + String.join(", ", subCommands.keySet()));
            }

            if (args.length > 1) {
                final String group = args[0];
                final String sub = args[1];

                if (group.equalsIgnoreCase("help")) {
                    getHelp(sub, commandSender);
                }

                if (subCommands.containsKey(group)) {
                    final Class<? extends AbstractCommandGroup> subCommandClass = subCommands.get(group).getClass();
                    final CommandProcessor commandProcessor = new CommandProcessor(plugin, subCommandClass);

                    commandProcessor.executeSubCommands(commandSender, sub, Arrays.copyOfRange(args, 2, args.length));
                }

            }

        }

        return true;
    }
}
