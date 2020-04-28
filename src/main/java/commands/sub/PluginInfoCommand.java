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
