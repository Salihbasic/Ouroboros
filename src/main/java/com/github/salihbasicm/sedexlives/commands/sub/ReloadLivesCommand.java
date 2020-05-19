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

package com.github.salihbasicm.sedexlives.commands.sub;

import com.github.salihbasicm.sedexlives.commands.AbstractSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.github.salihbasicm.sedexlives.SedexLives;
import com.github.salihbasicm.sedexlives.util.SedexLivesPermissions;

public class ReloadLivesCommand extends AbstractSubCommand {


    public ReloadLivesCommand(SedexLives lives) {
        super(lives);
    }

    /*
    Defines the sub-command "reload" used to reload the config.

    Usage: /lives reload
     */

    @Override
    public String getHelp() {
        return formatHelp("/lives reload",
                "Reloads the config.");
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
