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

package commands.sub;

import commands.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import sedexlives.SedexLives;

public class PluginInfoCommand extends AbstractSubCommand {

    private SedexLives plugin = SedexLives.getSedexLives();

    @Override
    public String getHelp() {
        return ChatColor.RED + "/lives info " + ChatColor.WHITE + "- " + ChatColor.GREEN + "Displays plugin info.\n";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("info")) {

            if (args.length > 2) {
                tooManyArguments(commandSender, getHelp());
                return true;
            }

            final String pluginInfo =
                    ChatColor.RED + "Name: " + ChatColor.GREEN + plugin.getDescription().getName() + "\n" +
                            ChatColor.RED + "Version: " + ChatColor.GREEN + plugin.getDescription().getVersion() + "\n" +
                            ChatColor.RED + "Author: " + ChatColor.GREEN + String.join(", ",
                            plugin.getDescription().getAuthors());

            commandSender.sendMessage(pluginInfo);

        }

        return true;
    }
}
