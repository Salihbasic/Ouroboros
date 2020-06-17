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

package com.github.salihbasicm.ouroboros.commands.sub;

import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.commands.AbstractSubCommand;
import com.github.salihbasicm.ouroboros.lang.Message;
import com.github.salihbasicm.ouroboros.util.OuroborosPermissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadLivesCommand extends AbstractSubCommand {


    public ReloadLivesCommand(Ouroboros ouroboros) {
        super(ouroboros);
    }

    /*
    Defines the sub-command "reload" used to reload the config.

    Usage: /lives reload [file]
     */

    @Override
    public String getHelp() {
        return formatHelp("/lives reload [file]",
                plugin.getOuroborosMessage().getSimpleMessage(Message.LIVES_RELOAD_HELP));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("reload")) {

            if (hasNoPermission(commandSender, OuroborosPermissions.RELOAD_CONFIG)) {
                return true;
            }

            if (args.length > 2) {
                tooManyArguments(commandSender);
                return true;
            }

            if (args.length == 2) {

                if (args[1].equalsIgnoreCase("messages")) {

                    plugin.debugMessage("Attempting to reload config.");
                    plugin.getOuroborosMessage().reloadMessages();
                    plugin.debugMessage("Successfully reloaded config!");

                    return true;

                }

            }

            plugin.debugMessage("Attempting to reload config.");
            plugin.reloadConfig();
            plugin.debugMessage("Successfully reloaded config!");

        }

        return true;
    }
}
