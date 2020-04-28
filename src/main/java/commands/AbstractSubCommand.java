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

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import sedexlives.SedexLives;

/**
 * Represents an abstract SedexLives sub-command.
 */
public abstract class AbstractSubCommand implements CommandExecutor  {

    /*

    Help messages should follow the format:

    ChatColor.RED + "/lives <subcommand> <args...>" + ChatColor.WHITE + "- " + ChatColor.GREEN + "command description"

    To get:

    [RED](command) [WHITE]- [GREEN]description

     */

    /**
     * Returns the help message of the command. Help message ought to be formatted uniformly, since
     * the default help command also uses this method to fetch help for each command.
     *
     * @return Command help message
     */
    public abstract String getHelp();

    @Override
    public abstract boolean onCommand(CommandSender commandSender, Command command, String label, String[] args);

    /*
    Additional abstraction to check for permissions.
     */
    public boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission(permission);
    }

    /*
    Message sent if there is no permission found.
     */
    public void noPermissionMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You do not have the required permissions to execute this command!");
    }

    /*
    Message sent if the player is not found.
     */
    public void playerNotFound(CommandSender sender, String username) {
        sender.sendMessage(ChatColor.RED + "Could not find player " + username + "!");
    }

    /*
    Message sent if the command does not have enough provided arguments.
     */
    public void notEnoughArguments(CommandSender sender, String help) {
        sender.sendMessage(ChatColor.RED + "Not enough arguments!");
        sender.sendMessage(ChatColor.RED + "Correct usage\n" + help);
    }

    /*
    Message sent if the command has too many provided arguments.
     */
    public void tooManyArguments(CommandSender sender, String help) {
        sender.sendMessage(ChatColor.RED + "Too many arguments!");
        sender.sendMessage(ChatColor.RED + "Correct usage:\n" + help);
    }

    /**
     * Iteraters over {@link Player}'s permissions and attempts to find {@code 'sedexlives.maxlives.#'} permission.
     * It then splits this permission and attempts to return number in place of {@code '#'}. If there is no number there,
     * or if the player has no permission, it returns the default maximum lives.
     *
     * @param player Player to be checked
     * @return Player's maximum lives
     */
    public int getPlayerMaxLives(Player player) {

        for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {

            if (perm.getPermission().startsWith("sedexlives.maxlives.")) {

                String[] split = perm.getPermission().split("\\.");
                if (split.length >= 3) {
                    return Integer.parseInt(split[2]);
                }

            }

        }

        return SedexLives.getSedexLives().getConfigManager().getDefaultLives();
    }
}
