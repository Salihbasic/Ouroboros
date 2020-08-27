package com.github.salihbasicm.ouroboros.commands.sub;

import com.github.salihbasicm.ouroboros.Ouroboros;
import com.github.salihbasicm.ouroboros.commands.AbstractCommandGroup;
import com.github.salihbasicm.ouroboros.commands.processor.SenderType;
import com.github.salihbasicm.ouroboros.commands.processor.SubCommand;
import com.github.salihbasicm.ouroboros.messages.Message;
import com.github.salihbasicm.ouroboros.util.OuroborosItem;
import com.github.salihbasicm.ouroboros.util.OuroborosPermissions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.Set;

public class ItemCommandGroup extends AbstractCommandGroup {

    public ItemCommandGroup(Ouroboros plugin) {
        super(plugin);
    }

    @SubCommand(
            sub = "list",
            usage = "/lives item list",
            help = "Lists all registered Ouroboros items.",
            permission = OuroborosPermissions.ITEM_LIST
    )
    private void listItems(final CommandSender sender, final String[] args) {
        final Set<String> items = plugin.getOuroborosItemStorage().getRegisteredItems();

        sender.sendMessage(Message.ITEM_LIST.formatMessage(items.size(), String.join(", ", items)));
    }

    @SubCommand(
            sub = "add",
            reqArgs = 3,
            usage = "/lives item add <name> <add> <override>",
            help = "Creates a new Ouroboros item with the given name which will add specified amount of lives on use.",
            permission = OuroborosPermissions.ITEM_ADD,
            sender = SenderType.PLAYER
    )
    private void addItem(final CommandSender sender, final String[] args) {
        final String name = args[0];
        final int add = Integer.parseInt(args[1]);
        final boolean override = Boolean.parseBoolean(args[2]);

        final ItemStack item = OuroborosItem.createOuroborosItem(plugin, add, override,
                this.getItemInMainHand((Player) sender));

        plugin.getOuroborosItemStorage().saveItem(item, name);
        sender.sendMessage(Message.ITEM_SAVE_SUCCESS.formatMessage());
    }

    @SubCommand(
            sub = "give",
            reqArgs = 3,
            usage = "/lives item give <player> <name> <amount>",
            help = "Gives the user a specified amount of an Ouroboros item.",
            permission = OuroborosPermissions.ITEM_GET,
            sender = SenderType.PLAYER
    )
    private void giveItem(final CommandSender sender, final String[] args) {
        final Player target = Bukkit.getPlayer(args[0]);
        final String name = args[1];
        final int amount = Math.abs(Integer.parseInt(args[2]));

        if (target != null) {

            final ItemStack item = this.getItemByName(name, sender);
            if (item == null)
                return;

            item.setAmount(amount);
            target.getInventory().addItem(item);

        } else { // target == null

            playerNotFound(sender, args[0]);

        }
    }

    @SubCommand(
            sub = "check",
            reqArgs = 1,
            usage = "/lives item check <name>",
            help = "Gives information about specified Ouroboros item.",
            permission = OuroborosPermissions.ITEM_CHECK
    )
    private void checkItem(final CommandSender sender, final String[] args) {
        final String name = args[0];

        final ItemStack item = this.getItemByName(name, sender);
        if (item == null)
            return;

        final int add = OuroborosItem.getAddFromOuroborosItem(plugin, item);
        final boolean override = OuroborosItem.getOverrideFromOuroborosItem(plugin, item);

        sender.sendMessage(Message.ITEM_INFO_INTRO.formatMessage(name));
        sender.sendMessage(Message.ITEM_INFO_ADD.formatMessage(add));
        sender.sendMessage(Message.ITEM_INFO_OVERRIDE.formatMessage(override));
    }

    @SubCommand(
            sub = "remove",
            reqArgs = 1,
            usage = "/lives item remove <name>",
            help = "Removes the specified Ouroboros item from storage.",
            permission = OuroborosPermissions.ITEM_REMOVE
    )
    private void removeItem(final CommandSender sender, final String[] args) {
        final String name = args[0];

        final ItemStack item = this.getItemByName(name, sender);
        if (item == null)
            return;

        plugin.getOuroborosItemStorage().removeItem(name);
        sender.sendMessage(Message.ITEM_REMOVE_SUCCESS.formatMessage(name));
    }

    private ItemStack getItemByName(final String name, final CommandSender sender) {
        if (plugin.getOuroborosItemStorage().getRegisteredItems().contains(name)) {

            return Objects.requireNonNull(plugin.getOuroborosItemStorage().getItem(name),
                    "Expected item, got null!");

        } else {

            sender.sendMessage(Message.ITEM_NOT_EXIST.formatMessage(name));
            return null;

        }
    }

    private ItemStack getItemInMainHand(final Player player) {
        final ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {

            player.sendMessage(Message.ITEM_USING_AIR.formatMessage());
            throw new IllegalArgumentException("Air lets you live, it doesn't give you a life dummy!");

        }

        return item;
    }

}
