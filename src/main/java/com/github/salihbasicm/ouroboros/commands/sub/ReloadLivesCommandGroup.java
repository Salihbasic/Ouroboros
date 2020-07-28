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
import com.github.salihbasicm.ouroboros.commands.AbstractCommandGroup;
import com.github.salihbasicm.ouroboros.commands.processor.SubCommand;
import com.github.salihbasicm.ouroboros.util.OuroborosPermissions;
import org.bukkit.command.CommandSender;

public class ReloadLivesCommandGroup extends AbstractCommandGroup {

    public ReloadLivesCommandGroup(Ouroboros ouroboros) {
        super(ouroboros);
    }

    /*
    Group: reload
    Description: Reloads plugin files
    Provides sub-commands:
        * config - reloads plugin config.yml
        * message - reloads plugin message.yml
     */

    @SubCommand(
            sub = "config",
            usage = "/lives reload config",
            help = "Reloads the plugin config.",
            permission = OuroborosPermissions.RELOAD_CONFIG
    )
    private void reloadConfig(final CommandSender sender, final String[] args) {
        plugin.debugMessage("Reloading config.yml");
        plugin.reloadConfig();
    }

    @SubCommand(
            sub = "messages",
            usage = "/lives reload messages",
            help = "Reloads the plugin messages.",
            permission = OuroborosPermissions.RELOAD_CONFIG
    )
    private void reloadMessages(final CommandSender sender, final String[] args) {
        plugin.debugMessage("Reloading messages.yml");
        plugin.getOuroborosMessage().reloadMessages();
    }
}
