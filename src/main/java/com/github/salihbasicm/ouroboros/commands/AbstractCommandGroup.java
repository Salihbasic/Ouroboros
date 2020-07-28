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
import org.bukkit.command.CommandSender;

/**
 * <p>Ouroboros commands are represented in forms of groups and sub-commands contained within those groups.
 * User needs to input the command form of: {@code /lives <group> <sub-command>}.</p>
 *
 * <p>When the user provides input, command processor checks all sub-commands within the group to find the
 * one which matches its name and arguments.</p>
 *
 * <p>Every command group does one thing and one thing only, with sub-commands only being variants.
 * For example there being a {@code check} group which checks lives, its sub-commands being {@code self} and
 * {@code player} which check the lives of either the sender (them)self or the specified player.</p>
 *
 * <p>EVery command group is marked as such by extending this abstract class and then being registered within
 * {@link OuroborosCommand}.</p>
 */
public abstract class AbstractCommandGroup {

    protected Ouroboros plugin;

    protected AbstractCommandGroup(Ouroboros plugin) {
        this.plugin = plugin;
    }

    /**
     * Sends a {@code USER_NOT_FOUND} message to the sender.
     * @param sender Command sender
     */
    protected final void playerNotFound(CommandSender sender) {
        sender.sendMessage(plugin.getOuroborosMessage().getSimpleMessage(Message.USER_NOT_FOUND));
    }

}
